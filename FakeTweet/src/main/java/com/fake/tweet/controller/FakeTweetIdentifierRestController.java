package com.fake.tweet.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fake.tweet.entity.TweetCredibilityStatistics;
import com.fake.tweet.evaluators.J48Evaluator;
import com.fake.tweet.evaluators.SVMEvaluator;
import com.fake.tweet.featuresetcreator.ContentUpdater;
import com.fake.tweet.featuresetcreator.FeatureSetCreator;
import com.fake.tweet.featuresetcreator.FeatureVectorUpdater;
import com.fake.tweet.featuresetcreator.LabelUpdater;
import com.fake.tweet.featuresetcreator.SentimentalScoreUpdater;
import com.fake.tweet.featuresetcreator.UnknownClassUpdater;
import com.fake.tweet.featuresetcreator.UserFeatureUpdater;
import com.fake.tweet.orchestrator.ExperimentOrchestrator;
import com.fake.tweet.orchestrator.ReportGenerator;
import com.fake.tweet.orchestrator.TweetContentFetcher;
import com.fake.tweet.pojo.ExperimentOrchestratorResult;
import com.fake.tweet.pojo.FeatureSetCreatorResult;
import com.fake.tweet.pojo.TweetContentFetcherResult;
import com.fake.tweet.service.TweetCredibilityStatisticsService;
import com.fake.tweet.utils.AppConfig;
import com.fake.tweet.utils.TextToImage;
import com.fasterxml.jackson.databind.ObjectMapper;

import twitter4j.JSONObject;
import weka.classifiers.Classifier;
import weka.classifiers.functions.SMO;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.SerializationHelper;

@RestController
@CrossOrigin
public class FakeTweetIdentifierRestController {

    @Autowired
    private FeatureSetCreator featureSetCreator;

    @Autowired
    private TweetContentFetcher tweetContentFetcher;

    @Autowired
    private TweetCredibilityStatisticsService tweetCredibilityStatisticsService;

    private String captcha;

    @RequestMapping(value = "/analyzeTweet", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public String analyzeTweetMobile(@RequestBody String requestBody) throws Exception {
        JSONObject json = new JSONObject();
        JSONObject requestBodyJson = new JSONObject(requestBody);
        System.out.println("analyzeTweet - in");
        // Get tweet id and update URL in map
        String tweetId = getTweetIdFromUrl(requestBodyJson.getString("url"));
        tweetCredibilityStatisticsService.saveTweetCredibilityStatistics(tweetId);
        if (tweetId != null) {
            json.put("tweetId", tweetId);
            // Construct feature set and add to model
            FeatureSetCreatorResult featureSetCreatorResult = constructFeatureSetForSingleTweet(tweetId);
            if (featureSetCreatorResult.getErrorMessage() != null) {
                json.put("error", featureSetCreatorResult.getErrorMessage());
                return json.toString();
            }

            json.put("featurevector", new ObjectMapper()
                    .writeValueAsString(featureSetCreatorResult.getTweetIdToFeatureVector().get(tweetId)));

            // Make predictions and add to model
            String j48PredictedClass = classifyInstance("j48.model", J48.class,
                    featureSetCreatorResult.getFeatureSet());
            String svmPredictedClass = classifyInstance("svm.model", SMO.class,
                    featureSetCreatorResult.getFeatureSet());
            json.put("j48PredictedClass", j48PredictedClass);
            json.put("svmPredictedClass", svmPredictedClass);

            TweetCredibilityStatistics tweetCredibilityStatistics = tweetCredibilityStatisticsService
                    .getTweetCredibilityStatistics(tweetId);
            if (tweetCredibilityStatistics != null) {
                json.put("realCount", tweetCredibilityStatistics.getRealCount());
                json.put("fakeCount", tweetCredibilityStatistics.getFakeCount());
            } else {
                json.put("realCount", 0);
                json.put("fakeCount", 0);
            }
        } else {
            json.put("error", "Tweet status URL is invalid");
        }
        System.out.println("analyzeTweet - out");
        return json.toString();
    }

    @RequestMapping(value = "/recordpoll", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity<String> recordPoll(@RequestBody String requestBody) throws Exception {
        JSONObject json = new JSONObject();
        JSONObject requestBodyJson = new JSONObject(requestBody);
        String tweetId = requestBodyJson.getString("tweetId");
        String vote = requestBodyJson.getString("vote");
        tweetCredibilityStatisticsService.saveTweetCredibilityStatistics(tweetId, vote);
        return new ResponseEntity(json.put("response", "success").toString(), HttpStatus.OK);
    }

    @RequestMapping(value = "/report", method = RequestMethod.GET, produces = "application/pdf")
    public ResponseEntity<byte[]> getReport() throws Exception {
        ExperimentOrchestrator experimentOrchestrator = ExperimentOrchestrator.builder()
                .featureSetCreator(new FeatureSetCreator()).j48Evaluator(new J48Evaluator())
                .svmEvaluator(new SVMEvaluator()).twitterContentFetcher(new TweetContentFetcher()).build();
        ExperimentOrchestratorResult experimentOrchestratorResult = experimentOrchestrator
                .orchestrateExperiment(tweetCredibilityStatisticsService.getAllTweetIds(), tweetCredibilityStatisticsService);
        SerializationHelper.write(AppConfig.getConfig("J48_MODEL_LOCATION"),
                experimentOrchestratorResult.getJ48Evaluation().getClassifier());
        SerializationHelper.write(AppConfig.getConfig("SVM_MODEL_LOCATION"),
                experimentOrchestratorResult.getSvmEvaluation().getClassifier());
        new ReportGenerator().generateReport(experimentOrchestratorResult.getJ48Evaluation(),
                experimentOrchestratorResult.getSvmEvaluation());
        Path path = Paths.get(AppConfig.getConfig("TEST_DATASET") + "FakeTweetJ48vsSVMReport.pdf");
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("Content-Disposition", "inline;filename=FakeTweetJ48vsSVMReport.pdf");
        ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(resource.getByteArray(), headers, HttpStatus.OK);
        return response;
    }

    @RequestMapping(value = "/getCaptcha", method = RequestMethod.GET, produces = "text/html")
    public byte[] getCaptcha() {
        captcha = getRandomString();
        BufferedImage bufferedImage = TextToImage.getImageFromText(captcha);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "png", os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        try {
            return IOUtils.toByteArray(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/validateCaptcha", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public String validateCaptcha(@RequestBody String requestBody) {
        JSONObject json = new JSONObject();
        JSONObject requestBodyJson = new JSONObject(requestBody);
        String captchaInput = requestBodyJson.getString("captcha");
        if (captcha.equalsIgnoreCase(captchaInput)) {
            json.put("response", "success");
        } else {
            captcha = getRandomString();
            json.put("response", captcha);
        }
        return json.toString();
    }

    private String getRandomString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 4) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    private FeatureSetCreatorResult constructFeatureSetForSingleTweet(String tweetId) throws Exception {
        TweetContentFetcherResult tweetContentFetcherResult = tweetContentFetcher
                .fetchTweetContents(Collections.singletonList(tweetId), tweetCredibilityStatisticsService);
        if (tweetContentFetcherResult.getTweetContents().isEmpty()) {
            FeatureSetCreatorResult featureSetCreatorResult = new FeatureSetCreatorResult();
            featureSetCreatorResult.setErrorMessage(tweetContentFetcherResult.getErrorMessage());
            return featureSetCreatorResult;
        }

        return featureSetCreator.createFeatureSet(tweetContentFetcherResult,
                AppConfig.getConfig("TEST_DATASET") + "testDataset.arff", new ArrayList<FeatureVectorUpdater>() {
                    {
                        add(new LabelUpdater());
                        add(new UnknownClassUpdater());
                        add(new ContentUpdater());
                        add(new SentimentalScoreUpdater());
                        add(new UserFeatureUpdater());
                    }
                });
    }

    private <T extends Classifier> String classifyInstance(String modelFilePath, Class<T> classifierType,
            Instances featureSet) throws Exception {
        modelFilePath = adjustModelFilePath(modelFilePath);
        Classifier classifier = classifierType.cast(SerializationHelper.read(modelFilePath));
        double predictedClass = classifier.classifyInstance(featureSet.get(0));
        String className = featureSet.attribute(featureSet.numAttributes() - 1).value((int) predictedClass);
        return className;
    }

    private String adjustModelFilePath(String modelFilePath) {
        File f = new File(modelFilePath);
        if (!(f.exists() && !f.isDirectory())) {
            modelFilePath = AppConfig.getConfig("MODEL_LOCATION") + modelFilePath;
        }
        return modelFilePath;
    }

    private String getTweetIdFromUrl(String url) {
        url = url.trim();
        Pattern pattern = Pattern.compile(".*/status/(\\d+)(/*.*)");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

}
