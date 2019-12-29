package com.fake.tweet.orchestrator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fake.tweet.entity.TweetCredibilityStatistics;
import com.fake.tweet.pojo.TweetContent;
import com.fake.tweet.pojo.TweetContentFetcherResult;
import com.fake.tweet.pojo.UserDetails;
import com.fake.tweet.service.TweetCredibilityStatisticsService;
import com.fake.tweet.utils.AppConfig;
import com.fake.tweet.utils.Utilities;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

/**
 * Created by saranyakrishnan on 11/5/17.
 */
@Service
public class TweetContentFetcher {

    /**
     * Get the content from local disk if present or take it from twitter API, store
     * it in disk and return in form of TweetContent pojo.
     *
     * @param tweetIds Twitter Ids to process
     * @return List of tweet contents
     */
    public TweetContentFetcherResult fetchTweetContents(List<String> tweetIds,
            TweetCredibilityStatisticsService tweetCredibilityStatisticsService)
            throws IOException, InterruptedException {

        TweetContentFetcherResult tweetContentFetcherResult = new TweetContentFetcherResult();

        List<TweetContent> tweetContents = new ArrayList<>();

        // Get content from twitter and construct tweetcontent for every tweet.
        // If tweet is in disk make TweetContent with that
        // else take it from twitter API
        for (String tweetId : tweetIds) {
            String content = null;
            if (Utilities.isFileExists(tweetId, AppConfig.getConfig("TWEET_CONTENTS_FOLDER"))) {
                // Read from file
                content = Utilities.readContentFromFile(tweetId, AppConfig.getConfig("TWEET_CONTENTS_FOLDER"));
            } else {
                // Fetch from twitter and write it in the main folder, if the content in the
                // internet is null isContentSuccessfullyDownloaded will
                // be false , So it will not be written in the twitter main data folder
                boolean isContentSuccessfullyDownloaded = fetchAndWriteTweetContents(tweetId);
                if (isContentSuccessfullyDownloaded) {
                    content = Utilities.readContentFromFile(tweetId, AppConfig.getConfig("TWEET_CONTENTS_FOLDER"));
                }
            }
            if (content != null && !content.isEmpty()) {
                TweetContent tweetContent = getTweetDetails(content, tweetContentFetcherResult);
                TweetCredibilityStatistics tweetCredibilityStatistics = tweetCredibilityStatisticsService
                        .getTweetCredibilityStatistics(tweetId);
                if(null != tweetCredibilityStatistics) {
                    if (tweetCredibilityStatistics.getFakeCount() > tweetCredibilityStatistics.getRealCount()) {
                        tweetContent.setLabel("fake");
                    } else if (tweetCredibilityStatistics.getRealCount() > tweetCredibilityStatistics.getFakeCount()) {
                        tweetContent.setLabel("real");
                    } else if (tweetCredibilityStatistics.getRealCount() == tweetCredibilityStatistics.getFakeCount()) {
                        tweetContent.setLabel("real");
                    }
                    if (tweetContent.getTweetId() != null) {
                        tweetContents.add(tweetContent);
                    } 
                }
            } else {
                tweetContentFetcherResult.setErrorMessage(
                        "The tweet content could not be pulled. Probably the tweet doesn't exist anymore or the account is suspended");
            }
        }

        tweetContentFetcherResult.setTweetContents(tweetContents);

        return tweetContentFetcherResult;
    }

    // Retrieve tweet details using tweet ID and write it into a txt file with file
    // name as tweet ID
    private static Status getTweetDetailsByID(String pTweetID) {
        final Twitter twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(AppConfig.getConfig("CONSUMER_KEY"), AppConfig.getConfig("CONSUMER_KEY_SECRET"));
        AccessToken accessToken = new AccessToken(AppConfig.getConfig("TWITTER_ACCESS_TOKEN"),
                AppConfig.getConfig("TWITTER_ACCESS_TOKEN_SECRET"));
        twitter.setOAuthAccessToken(accessToken);
        try {
            Status status = twitter.showStatus(Long.parseLong(pTweetID));
            if (status == null) { //
                // don't know if needed - T4J docs are very bad
            } else {
                return status;
            }
        } catch (TwitterException e) {
            // return e.getMessage();
            // e.printStackTrace();
            // DON'T KNOW IF THIS IS THROWN WHEN ID IS INVALID
        }
        return null;
    }

    // Write Tweet details
    private boolean fetchAndWriteTweetContents(String pTweetID)
            throws FileNotFoundException, UnsupportedEncodingException {
        Status tweetDetails = getTweetDetailsByID(pTweetID);
        Gson gson = new Gson();
        String fileName = AppConfig.getConfig("TWEET_CONTENTS_FOLDER") + "/" + pTweetID + ".txt";
        if (tweetDetails != null) {
            try {
                Files.write(Paths.get(fileName), gson.toJson(tweetDetails).getBytes());
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;

    }

    // Read the tweetDetails and extract tweet details
    private TweetContent getTweetDetails(String fileContent, TweetContentFetcherResult tweetContentFetcherResult)
            throws IOException, InterruptedException {

        TweetContent tweetContent = new TweetContent();

        Gson gson = new Gson();
        // JsonElement element = gson.fromJson(fileContent, JsonElement.class);
        // JsonElement element = gson.fromJson(fileContent, JsonObject.class);

        // JsonObject object = element.getAsJsonObject();
        JsonObject object = gson.fromJson(fileContent, JsonObject.class);

        // Get language
        JsonElement lang = object.get("lang");
        // tweetContent.setTweetLang(lang.getAsString());

        if (lang.getAsString().equals("en")) {

            // Get tweet ID
            JsonElement tweetID = object.get("id");
            tweetContent.setTweetId(tweetID.getAsString());

            JsonElement tweetCreatedDate = object.get("createdAt");
            tweetContent.setCreatedDate(tweetCreatedDate.getAsString());

            JsonObject reTweetObject = object.getAsJsonObject("retweetedStatus");

            // Get Images from Retweeted Status
            List<String> mediaURL = new ArrayList<>();
            if (reTweetObject != null) {
                JsonArray reTweetedMediaEnitites = reTweetObject.getAsJsonArray("mediaEntities");
                if (!reTweetedMediaEnitites.isJsonNull() && reTweetedMediaEnitites.size() != 0) {
                    List<String> entities = getTweetImages(reTweetedMediaEnitites);
                    mediaURL.addAll(entities);
                }
            }

            // Get Images
            JsonArray mediaEntities = object.getAsJsonArray("mediaEntities");
            if (!mediaEntities.isJsonNull() && mediaEntities.size() != 0) {
                List<String> entities = getTweetImages(mediaEntities);
                mediaURL.addAll(entities);
            }

            if (mediaURL.size() > 0)
                tweetContent.setMediaEntities(mediaURL);

            // Get URL from Retweeted Status
            List<String> urlList = new ArrayList<>();
            if (reTweetObject != null) {
                JsonArray reTweetedURLEnitites = reTweetObject.getAsJsonArray("urlEntities");
                if (!reTweetedURLEnitites.isJsonNull() && reTweetedURLEnitites.size() != 0) {
                    List<String> entities = getTweetURL(reTweetedURLEnitites);
                    urlList.addAll(entities);
                }
            }

            // Get URL
            JsonArray urlEntities = object.getAsJsonArray("urlEntities");
            if (!urlEntities.isJsonNull() && urlEntities.size() != 0) {
                List<String> entities = getTweetURL(urlEntities);
                urlList.addAll(entities);
            }

            if (urlList.size() > 0) {
                tweetContent.setUrlEntities(urlList);
            }

            // Get hashtags
            JsonArray hashtagEntities = object.getAsJsonArray("hashtagEntities");
            if (!hashtagEntities.isJsonNull() && hashtagEntities.size() != 0) {
                List<String> entities = getTweetHashTags(hashtagEntities);
                tweetContent.setHashtagEntities(entities);
            }

            JsonElement tweetText = object.get("text");
            tweetContent.setTweetText(tweetText.getAsString());

            JsonElement retweetCount = object.get("retweetCount");
            tweetContent.setRetweetCount(retweetCount.getAsString());

//       User Details
            UserDetails userDetails = new UserDetails();
            JsonObject userObject = object.getAsJsonObject("user");
            JsonElement friendsCount = userObject.get("friendsCount");
            userDetails.setNoOfFriends(friendsCount.getAsInt());
            JsonElement followersCount = userObject.get("followersCount");
            userDetails.setNoOfFollowers(followersCount.getAsInt());
            JsonElement timesListed = userObject.get("listedCount");
            userDetails.setNoOfTimesListed(timesListed.getAsInt());
            JsonElement isVerifiedUser = userObject.get("isVerified");
            userDetails.setVerifiedUser(isVerifiedUser.getAsBoolean());
            JsonElement tweetCount = userObject.get("statusesCount");
            userDetails.setNoOfTweets(tweetCount.getAsInt());
            JsonElement id = userObject.get("id");
            userDetails.setUserID(id.getAsString());
            JsonElement name = userObject.get("name");
            userDetails.setUserName(name.getAsString());
            // JsonElement screenName = userObject.get("screenName");
            JsonObject urlObj = userObject.getAsJsonObject("urlEntity");
            if (urlObj != null) {
                JsonElement hasURL = urlObj.get("url");
                if (hasURL != null)
                    userDetails.setUserHasURL(true);
            }
            if (followersCount.getAsInt() != 0) {
                double friendFollowerRatio = friendsCount.getAsInt() / followersCount.getAsInt() * 1.0;
                userDetails.setFriendFollowerRatio(friendFollowerRatio);
            }
            tweetContent.setUserDetails(userDetails);
        } else {
            tweetContentFetcherResult
                    .setErrorMessage("Language of tweet does not seem to be english. We only support english tweets.");
        }
        return tweetContent;
    }

    // Get media entities
    private List<String> getTweetImages(JsonArray mediaEntities) throws IOException, InterruptedException {
        List<String> mediaURLs = new ArrayList<>();
        for (int i = 0; i < mediaEntities.size(); i++) {
            JsonObject mediaObject = mediaEntities.get(i).getAsJsonObject();
            // check "mediaURLHttps" or "expandedURL" for media
            mediaURLs.add(mediaObject.get("mediaURLHttps").getAsString());
        }
        return mediaURLs;
    }

    // Get url entities
    private List<String> getTweetURL(JsonArray urlEntities) throws IOException {
        List<String> urls = new ArrayList<>();
        for (int i = 0; i < urlEntities.size(); i++) {
            JsonObject urlObject = urlEntities.get(i).getAsJsonObject();
            // check "url" or "expandedURL"
            urls.add(urlObject.get("expandedURL").getAsString());
        }
        return urls;
    }

    // Get Hashtag entities
    private List<String> getTweetHashTags(JsonArray hashtagEntities) throws IOException {
        List<String> urls = new ArrayList<>();
        for (int i = 0; i < hashtagEntities.size(); i++) {
            JsonObject urlObject = hashtagEntities.get(i).getAsJsonObject();
            // check "url" or "expandedURL"
            urls.add(urlObject.get("text").getAsString());
        }
        return urls;
    }
}
