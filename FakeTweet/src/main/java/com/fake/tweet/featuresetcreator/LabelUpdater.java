package com.fake.tweet.featuresetcreator;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fake.tweet.pojo.FeatureVector;
import com.fake.tweet.pojo.GoogleDocumentSearchResult;
import com.fake.tweet.pojo.GoogleImageSearchReport;
import com.fake.tweet.pojo.GoogleImageSearchResult;
import com.fake.tweet.pojo.TweetContent;
import com.fake.tweet.utils.AppConfig;
import com.fake.tweet.utils.FakeUrlDetector;
import com.fake.tweet.utils.Utilities;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;

public class LabelUpdater implements FeatureVectorUpdater {
    @Override
    public void updateFeatureVector(FeatureVector featureVector, TweetContent tweetContent) throws Exception {

        if (tweetContent.getMediaEntities() != null) {
            featureVector.setContainsImages(true);
        } else {
            featureVector.setContainsImages(false);
        }

        if (tweetContent.getUrlEntities() != null) {
            featureVector.setContainsUrl(true);
        } else {
            featureVector.setContainsUrl(false);
        }

        boolean isUrlCredible = isUrlCredible(tweetContent);
        boolean isImageCredible = isImageCredible(tweetContent, featureVector);

//        String label = "fake";
//        if (isUrlCredible && isImageCredible) {
//            label = "real";
//        }

        // Get Label from Ground Truth file
//        HashMap<String, String> groundTruthMap = readGroundTruthResults();
//        String label = groundTruthMap.get(tweetContent.getTweetId());
//        featureVector.setLabel(label);

        // Set in feature vector
        if (isImageCredible) {
            featureVector.setIsImageCredible(1);

        } else {
            featureVector.setIsImageCredible(0);
        }
        if (isUrlCredible) {
            featureVector.setIsUrlCredible(1);
        } else {
            featureVector.setIsUrlCredible(0);
        }
        featureVector.setLabel(tweetContent.getLabel());
    }

    private boolean isUrlCredible(TweetContent tweetContent) throws MalformedURLException {
        List<String> urlList = tweetContent.getUrlEntities();
        if (urlList != null) {
            for (String url : urlList) {
                if (FakeUrlDetector.isUrlInFakeWebsitesList(url)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isImageCredible(TweetContent tweetContent, FeatureVector featureVector) throws Exception {
        if (doesDocumentSearchReportFake(tweetContent, featureVector)) {
            return false;
        } else if (doesImageSearchReportFake(tweetContent, featureVector)) {
            return false;
        }
        return true;
    }

    private boolean doesDocumentSearchReportFake(TweetContent tweetContent, FeatureVector featureVector)
            throws Exception {
        List<GoogleDocumentSearchResult> googleDocumentSearchResults = getGoogleDocumentSearchResults(tweetContent);
        for (GoogleDocumentSearchResult googleDocumentSearchResult : googleDocumentSearchResults) {
            if (FakeUrlDetector.isUrlInFakeWebsitesList(googleDocumentSearchResult.getDocumentSearchResultUrl())) {
                featureVector.setDoesGoogleDocumentSearchForMediaEntityHaveFakeUrl(true);
                return true;
            } else if (containsIgnoreCase(googleDocumentSearchResult.getJson(), "fake")) {
                featureVector.setDoesGoogleDocumentSearchForMediaEntityHaveWordFake(true);
                return true;
            }
        }
        return false;
    }

    private List<GoogleDocumentSearchResult> getGoogleDocumentSearchResults(TweetContent tweetContent)
            throws Exception {
        // Either take google document search result from disk or from URL
        // For each search result (items in response) construct
        // GoogleDocumentSearchResult and return list
        List<String> entities = tweetContent.getMediaEntities();
        String fileName = "";
        if (entities != null && entities.size() > 0) {
            for (String mediaURL : entities) {
                String[] splitStr = mediaURL.split("//");
                String[] fileNameStr = splitStr[1].split("/");
                fileName = fileNameStr[2];
                if (!Utilities.isFileExists(fileName, AppConfig.getConfig("GOOGLE_CUSTOMSEARCH_OUTPUT_FOLDER"))) {
                    getGoogleDocumentSearchResult(mediaURL, AppConfig.getConfig("GOOGLE_CUSTOMSEARCH_URL"), fileName);
                }
            }
        }

        if (!fileName.equals("")) {
            // Read from file
            String fileContent = Utilities.readContentFromFile(fileName,
                    AppConfig.getConfig("GOOGLE_CUSTOMSEARCH_OUTPUT_FOLDER"));
            List<GoogleDocumentSearchResult> googleDocumentSearchResults = createGoogleDocumentSearchResults(
                    fileContent);
            return googleDocumentSearchResults;
        }
        return new ArrayList<>();
    }

    private List<GoogleDocumentSearchResult> createGoogleDocumentSearchResults(String fileContent) {
        List<GoogleDocumentSearchResult> googleDocumentSearchResults = new ArrayList<>();
        Gson gson = new Gson();
        JsonElement element = gson.fromJson(fileContent, JsonElement.class);
        JsonObject object = element.getAsJsonObject();

        JsonArray items = object.getAsJsonArray("items");
        if (items != null) {
            for (int i = 0; i < items.size(); i++) {
                JsonObject jsonObject = items.get(i).getAsJsonObject();
                GoogleDocumentSearchResult googleDocumentSearchResult = new GoogleDocumentSearchResult();
                googleDocumentSearchResult.setDocumentSearchResultUrl(jsonObject.get("link").getAsString());
                googleDocumentSearchResult.setJson(jsonObject.toString());
                googleDocumentSearchResults.add(googleDocumentSearchResult);
            }
        }
        return googleDocumentSearchResults;
    }

    // HTTP GET request
    private void getGoogleDocumentSearchResult(String query, String url, String fileName) throws Exception {
        String urlObj = url + "&q=" + query;
        URL obj = new URL(urlObj);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // optional default is GET
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        writeGoogleCustomSearchDetails(gson.toJson(gson.fromJson(response.toString(), JsonObject.class)), fileName);
    }

    // Write Google custom search details
    private void writeGoogleCustomSearchDetails(String response, String filename) throws IOException {
        String fileName = AppConfig.getConfig("GOOGLE_CUSTOMSEARCH_OUTPUT_FOLDER") + filename + ".txt";
        File file = new File(fileName);
        file.createNewFile();
        try {
            Files.write(Paths.get(fileName), response.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean doesImageSearchReportFake(TweetContent tweetContent, FeatureVector featureVector) throws Exception {
        List<GoogleImageSearchReport> googleImageSearchReports = getGoogleImageSearchReports(tweetContent);
        for (GoogleImageSearchReport googleImageSearchReport : googleImageSearchReports) {
            for (GoogleImageSearchResult googleImageSearchResult : googleImageSearchReport
                    .getGoogleImageSearchResults()) {
                if (FakeUrlDetector.isUrlInFakeWebsitesList(googleImageSearchResult.getImageSearchResultUrl())) {
                    featureVector.setDoesGoogleImageSearchForMediaEntityHaveFakeUrl(true);
                    return true;
                }
                if (containsIgnoreCase(googleImageSearchResult.getTitle(), "fake")) {
                    featureVector.setDoesGoogleImageSearchForMediaEntityHaveWordFakeInTitle(true);
                    return true;
                }
                if (containsIgnoreCase(googleImageSearchResult.getDescription(), "fake")) {
                    featureVector.setDoesGoogleImageSearchForMediaEntityHaveWordFakeInDescription(true);
                    return true;
                }
                if (!isTimelineAligned(googleImageSearchResult.getDate(), tweetContent)) {
                    featureVector.setDoesGoogleImageSearchForMediaEntityHaveDateMisaligned(true);
                    return true;
                }
            }
            if (!isBestGuessRelated(googleImageSearchReport.getBestGuess(), tweetContent)) {
                featureVector.setBestGuessForGoogleImageSearchForMediaEntityUnrelated(true);
                return false;
            }
        }
        return false;
    }

    private List<GoogleImageSearchReport> getGoogleImageSearchReports(TweetContent tweetContent) throws Exception {
        List<GoogleImageSearchReport> googleImageSearchReports = new ArrayList<>();
        List<String> entities = tweetContent.getMediaEntities();
        String fileName = "";
        if (entities != null && entities.size() > 0) {
            for (String mediaURL : entities) {
                String[] splitStr = mediaURL.split("//");
                String[] fileNameStr = splitStr[1].split("/");
                fileName = fileNameStr[2];
                GoogleImageSearchReport googleImageSearchReport = new GoogleImageSearchReport();
                String bestGuess = getGoogleBestGuess(fileName, mediaURL);
                List<GoogleImageSearchResult> googleImageSearchResults = getGoogleResultsThatContainsImages(fileName,
                        mediaURL);
                googleImageSearchReport.setBestGuess(bestGuess);
                googleImageSearchReport.setGoogleImageSearchResults(googleImageSearchResults);
                googleImageSearchReports.add(googleImageSearchReport);
            }
        }
        return googleImageSearchReports;
    }

    private List<GoogleImageSearchResult> getGoogleResultsThatContainsImages(String fileName, String mediaURL)
            throws Exception {
        List<GoogleImageSearchResult> goolgleImageSearchResults = new ArrayList<>();
        if (!Utilities.isFileExists(fileName, AppConfig.getConfig("GOOGLE_IMAGE_SEARCH_RESULTS_INPUTFOLDER"))) {
            goolgleImageSearchResults = getImageSearchResultsFromGoogleSearch(mediaURL);
            writeGoogleImageSearchResults(goolgleImageSearchResults, fileName);
        }
        goolgleImageSearchResults = readGoogleImageSearchResultsFromFile(
                AppConfig.getConfig("GOOGLE_IMAGE_SEARCH_RESULTS_INPUTFOLDER") + fileName + ".txt");
        return goolgleImageSearchResults;
    }

    private List<GoogleImageSearchResult> getImageSearchResultsFromGoogleSearch(String imageURL)
            throws IOException, InterruptedException {
        Thread.sleep(500);
        while (true) {
            try {
                List<GoogleImageSearchResult> goolgleImageSearchResults = new ArrayList<>();
                String newUrl = "http://www.google.com/searchbyimage?image_url=" + imageURL + "&h1=en";
                Document doc = Jsoup.connect(newUrl).userAgent(
                        "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.76 Safari/537.36")
                        .get();
                Elements PagesThatIncludeImages = doc.select("div.srg div.g");
                for (Element element : PagesThatIncludeImages) {
                    GoogleImageSearchResult goolgleImageSearchResult = new GoogleImageSearchResult();
                    String title = element.select("div.rc").select("h3.r").text();
                    String url = element.select("div.rc").select("div.s").select("cite._Rm").text();
                    String description = element.select("div.rc").select("div.s").select("span.st").text();
                    String date = element.select("div.rc").select("div.s").select("span.f").text();
                    goolgleImageSearchResult.setTitle(title);
                    goolgleImageSearchResult.setImageSearchResultUrl(url);
                    goolgleImageSearchResult.setDescription(description);
                    goolgleImageSearchResult.setDate(date);
                    goolgleImageSearchResults.add(goolgleImageSearchResult);
                }
                return goolgleImageSearchResults;
            } catch (Exception e) {
                int exceptionCode = handleException(e);
                if (exceptionCode != 503) {
                    return null;
                }

            }
        }
    }

    // Write Google Image search results
    private void writeGoogleImageSearchResults(List<GoogleImageSearchResult> response, String imageID)
            throws FileNotFoundException, UnsupportedEncodingException {
        String fileName = AppConfig.getConfig("GOOGLE_IMAGE_SEARCH_RESULTS_INPUTFOLDER") + imageID + ".txt";
        String contents = new Gson().toJson(response);
        try {
            Files.write(Paths.get(fileName), contents.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<GoogleImageSearchResult> readGoogleImageSearchResultsFromFile(String fileName) throws IOException {
        List<GoogleImageSearchResult> googleImageSearchResults = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null && !line.equals("null")) {
                sb.append(line);
                line = br.readLine();
            }
            if (sb != null && StringUtils.isNotEmpty(sb.toString()) && !sb.equals("null")) {
                JsonArray array = new Gson().fromJson(sb.toString(), JsonArray.class);
                for (int i = 0; i < array.size(); i++) {
                    GoogleImageSearchResult googleImageSearchResult = new GoogleImageSearchResult();
                    JsonObject object = array.get(i).getAsJsonObject();
                    googleImageSearchResult.setTitle(object.get("title").getAsString());
                    googleImageSearchResult.setDate(object.get("date").getAsString());
                    googleImageSearchResult.setImageSearchResultUrl(object.get("imageSearchResultUrl").getAsString());
                    googleImageSearchResult.setDescription(object.get("description").getAsString());
                    googleImageSearchResults.add(googleImageSearchResult);
                }
                return googleImageSearchResults;
            }
        } finally {
            br.close();
        }
        return googleImageSearchResults;
    }

    private boolean isTimelineAligned(String dateString, TweetContent tweetContent) throws ParseException {
        Date tweetCreatedDate = Utilities.convertTweetStringToDate(tweetContent.getCreatedDate());
        String[] dateStringArray = dateString.split("-");
        for (String date : dateStringArray) {
            Date googleTaggedDate = Utilities.convertGoogleStringToDate(date);
            if (googleTaggedDate != null) {
                Boolean isTweetCreatedDateBefore = Utilities.compareDate(tweetCreatedDate, googleTaggedDate);
                return isTweetCreatedDateBefore;
            }
        }
        return true;
    }

    private boolean isBestGuessRelated(String bestGuess, TweetContent tweetContent)
            throws IOException, InterruptedException {
        String tweetText = tweetContent.getTweetText();
        String[] bestGuessArr = bestGuess.split(" ");
        for (String bestGuessStr : bestGuessArr) {
            if (tweetText.toLowerCase().contains(bestGuessStr.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private String getGoogleBestGuess(String fileName, String imageURL) throws IOException, InterruptedException {
        String bestGuess = "";
        if (!Utilities.isFileExists(fileName, AppConfig.getConfig("GOOGLE_BESTGUESS_INPUT_FOLDER"))) {
            bestGuess = getBestGuessFromGoogleSearch(imageURL);
            // if(bestGuess != null && bestGuess.equalsIgnoreCase("null"))
            writeGoogleBestGuessDetails(bestGuess, fileName);
        }
        // if(bestGuess != null && bestGuess.equalsIgnoreCase("null"))
        bestGuess = readGoogleBestGuessFromFile(
                AppConfig.getConfig("GOOGLE_BESTGUESS_INPUT_FOLDER") + fileName + ".txt");
        return bestGuess;
    }

    private String readGoogleBestGuessFromFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String content = "";
        String bestGuess = "";
        try {
            content = new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
            if (!content.trim().equals("null")) {
                String[] lineStr = content.split(":");
                bestGuess = lineStr[1].trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            br.close();
        }
        return bestGuess;
    }

    private String getBestGuessFromGoogleSearch(String imageURL) throws IOException, InterruptedException {
        Thread.sleep(500);
        while (true) {
            String newUrl = "http://www.google.com/searchbyimage?image_url= " + imageURL;
            Document doc = null;
            String bestGuess = null;
            try {
                doc = Jsoup.connect(newUrl).userAgent(
                        "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.76 Safari/537.36")
                        .get();
                // Elements bestGuessElement = doc.select("._hUb");
                String documentContent = doc.toString();
                int bestGuessIndex = documentContent.indexOf("Best guess");
                int closingAnchorIndex = documentContent.indexOf("</a>", bestGuessIndex);
                String bestGuessSegment = documentContent.substring(bestGuessIndex, closingAnchorIndex + 4).trim();
                bestGuess = bestGuessSegment.replaceAll("<a[^>]*>([^<]+)<\\/a>", "$1");
//                if (!bestGuessElement.isEmpty() && bestGuessElement.hasText()) {
//                    bestGuess = bestGuessElement.text();
//                }
                return bestGuess;
            } catch (Exception e) {
                int exceptionCode = handleException(e);
                if (exceptionCode != 503) {
                    return null;
                }
            }
        }
    }

    private static int handleException(Exception e) throws InterruptedException {
        if (e instanceof HttpStatusException) {
            HttpStatusException exception = (HttpStatusException) e;
            System.out.println("HttpStatusException Found");
            System.out.println("The status is " + exception.getStatusCode());
            if (exception.getStatusCode() == 503) {
                System.out.println("Sleeping for two hours..");
                Thread.sleep(7200000);
            } else {
                System.out.println("Ignoring the exception and continuing ! ");
            }
            return exception.getStatusCode();
        }

        return 0;

    }

    // Write Google Best Guess details
    private static void writeGoogleBestGuessDetails(String response, String imageID)
            throws FileNotFoundException, UnsupportedEncodingException {
        String fileName = AppConfig.getConfig("GOOGLE_BESTGUESS_INPUT_FOLDER") + imageID + ".txt";
        String nullString = "null";
        try {
            if (response != null && !response.equalsIgnoreCase("null")) {
                Files.write(Paths.get(fileName), response.getBytes());
            } else {
                Files.write(Paths.get(fileName), nullString.getBytes());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static HashMap<String, String> readGroundTruthResults() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(AppConfig.getConfig("GROUND_TRUTH_FILE")));
        try {
            String line;
            HashMap<String, String> groundTruthMap = new HashMap<>();
            while ((line = br.readLine()) != null) {
                String[] lineStr = line.split(" ");
                groundTruthMap.put(lineStr[0], lineStr[1]);
            }
            return groundTruthMap;
        } finally {
            br.close();
        }
    }

}
