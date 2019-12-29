package com.fake.tweet.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.fake.tweet.entity.TweetCredibilityStatistics;
import com.fake.tweet.featuresetcreator.ContentUpdater;
import com.fake.tweet.featuresetcreator.FeatureSetCreator;
import com.fake.tweet.featuresetcreator.FeatureVectorUpdater;
import com.fake.tweet.featuresetcreator.LabelUpdater;
import com.fake.tweet.featuresetcreator.SentimentalScoreUpdater;
import com.fake.tweet.featuresetcreator.UnknownClassUpdater;
import com.fake.tweet.featuresetcreator.UserFeatureUpdater;
import com.fake.tweet.model.FakeTweetIdentifierModel;
import com.fake.tweet.orchestrator.TweetContentFetcher;
import com.fake.tweet.pojo.FeatureSetCreatorResult;
import com.fake.tweet.pojo.TweetContentFetcherResult;
import com.fake.tweet.service.TweetCredibilityStatisticsService;
import com.fake.tweet.utils.AppConfig;

import weka.classifiers.Classifier;
import weka.classifiers.functions.SMO;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.SerializationHelper;

@Controller
@CrossOrigin
public class FakeTweetIdentifierController {

    @Autowired
    private FeatureSetCreator featureSetCreator;

    @Autowired
    private TweetContentFetcher tweetContentFetcher;

    @Autowired
    private TweetCredibilityStatisticsService tweetCredibilityStatisticsService;

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "text/html")
    public ModelAndView fakeTweetIdentifier() {
        return new ModelAndView("faketweetidentifier", "command", new FakeTweetIdentifierModel());
    }

    @RequestMapping(value = "/tweet", method = RequestMethod.POST, produces = "text/html")
    public String tweet(@ModelAttribute("command") FakeTweetIdentifierModel fakeTweetIdentifierModel,
            ModelMap modelMap) {
        String tweetId = getTweetIdFromUrl(fakeTweetIdentifierModel.getUrl());
        modelMap.addAttribute("tweetId", tweetId);
        return "tweet";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, produces = "text/html")
    public String analyzeTweet(@ModelAttribute("command") FakeTweetIdentifierModel fakeTweetIdentifierModel,
            ModelMap modelMap) throws Exception {
        System.out.println("analyzeTweet - in");
        // Get tweet id and update URL in map
        String tweetId = getTweetIdFromUrl(fakeTweetIdentifierModel.getUrl());
        tweetCredibilityStatisticsService.saveTweetCredibilityStatistics(tweetId);
        if (tweetId != null) {

            modelMap.addAttribute("url", fakeTweetIdentifierModel.getUrl());
            modelMap.addAttribute("tweetId", tweetId);

            // Construct feature set and add to model
            FeatureSetCreatorResult featureSetCreatorResult = constructFeatureSetForSingleTweet(tweetId);
            if (featureSetCreatorResult.getErrorMessage() != null) {
                modelMap.addAttribute("error", featureSetCreatorResult.getErrorMessage());
                return "faketweetidentifier";
            }

            modelMap.addAttribute("featurevector", featureSetCreatorResult.getTweetIdToFeatureVector().get(tweetId));

            // Make predictions and add to model
            String j48PredictedClass = classifyInstance("j48.model", J48.class,
                    featureSetCreatorResult.getFeatureSet());
            String svmPredictedClass = classifyInstance("svm.model", SMO.class,
                    featureSetCreatorResult.getFeatureSet());
            modelMap.addAttribute("j48PredictedClass", j48PredictedClass);
            modelMap.addAttribute("svmPredictedClass", svmPredictedClass);

            TweetCredibilityStatistics tweetCredibilityStatistics = tweetCredibilityStatisticsService
                    .getTweetCredibilityStatistics(tweetId);
            if (tweetCredibilityStatistics != null) {
                modelMap.addAttribute("realCount", tweetCredibilityStatistics.getRealCount());
                modelMap.addAttribute("fakeCount", tweetCredibilityStatistics.getFakeCount());
            } else {
                modelMap.addAttribute("realCount", 0);
                modelMap.addAttribute("fakeCount", 0);
            }

            // TODO: Gotta check if this can be removed
            modelMap.addAttribute("command", fakeTweetIdentifierModel);
        } else {
            modelMap.addAttribute("error", "Tweet status URL is invalid");
        }
        System.out.println("analyzeTweet - out");
        return "faketweetidentifier";
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
