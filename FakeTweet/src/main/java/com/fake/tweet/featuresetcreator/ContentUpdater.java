package com.fake.tweet.featuresetcreator;

import com.fake.tweet.pojo.FeatureVector;
import com.fake.tweet.pojo.TweetContent;

/**
 * Created by saranyakrishnan on 11/7/17.
 * <p>
 * Updates the following fields in FeatureVector
 * ------------------------------------------------------------------------
 * 1. tweetLength
 * 2. wordCount
 * 3. noOfQuestionMark
 * 4. noOfExclamationMark
 * 5  containsQuestionMark
 * 6. containsExcalmationMark
 * 7. noOfUpperCaseLetter
 * 8. noOfhasgTags
 * 9. noOfUrls
 * 10. noOfRetweets
 */
public class ContentUpdater implements FeatureVectorUpdater {
    @Override
    public void updateFeatureVector(FeatureVector featureVector, TweetContent tweetContent) {

        String tweetText = tweetContent.getTweetText();
        String tweetID = tweetContent.getTweetId();
        featureVector.setTweetID(tweetID);
        int tweetLength = calculateTweetLength(tweetText);
        int wordCount = calculateWordCount(tweetText);
        int noOfQuestionMark = calculateNumberOfQuestionMark(tweetText);
        int noOfExclamationMark = calculateNumberOfExcalamtionMark(tweetText);
        boolean containsQuestionMark = doesContainsQuestionMark(tweetText);
        boolean containsExcalmationMark = doesContainExclamtionMark(tweetText);
        int noOfUpperCaseLetter = CalculateNumberOfUpperCaseLetter(tweetText);
        int noOfhasgTags = calculateNumberOfHashTags(tweetContent);
        int noOfUrls = calculateNumberOfUrl(tweetContent);
        int noOfRetweets = calculateNumberOfReTweets(tweetContent);

        //update featurevector
        featureVector.setTweetID(tweetID);
        featureVector.setTweetLength(tweetLength);
        featureVector.setWordCount(wordCount);
        featureVector.setNoOfQuestionMark(noOfQuestionMark);
        featureVector.setNoOfExclamationMark(noOfExclamationMark);
        if (containsExcalmationMark) {
            featureVector.setContainsExcalmationMark(1);

        } else {
            featureVector.setContainsExcalmationMark(0);

        }
        if (containsQuestionMark) {
            featureVector.setContainsQuestionMark(1);

        } else {
            featureVector.setContainsQuestionMark(0);

        }
        featureVector.setNoOfUpperCaseLetter(noOfUpperCaseLetter);
        featureVector.setNoOfhasgTags(noOfhasgTags);
        featureVector.setNoOfUrls(noOfUrls);
        featureVector.setNoOfRetweets(noOfRetweets);
    }

    private int calculateNumberOfQuestionMark(String tweetText) {
        int count = 0;
        for (int i = 0; i < tweetText.length(); i++) {
            if (tweetText.charAt(i) == '?') count++;
        }
        return count;
    }

    private int calculateWordCount(String tweetText) {
        return tweetText.split(" ").length;
    }

    private int calculateTweetLength(String tweetText) {
        return tweetText.length();
    }


    private int calculateNumberOfExcalamtionMark(String tweetText) {
        int count = 0;
        for (int i = 0; i < tweetText.length(); i++) {
            if (tweetText.charAt(i) == '!') count++;
        }
        return count;
    }

    private int CalculateNumberOfUpperCaseLetter(String tweetText) {
        int count = 0;
        for (int i = 0; i < tweetText.length(); i++) {
            if (Character.isUpperCase(tweetText.charAt(i))) {
                count++;
            }
        }
        return count;

    }

    private int calculateNumberOfHashTags(TweetContent tweetContent) {
        if (tweetContent.getHashtagEntities() == null) return 0;
        return tweetContent.getHashtagEntities().size();

    }

    private int calculateNumberOfUrl(TweetContent tweetContent) {
        if (tweetContent.getUrlEntities() == null) return 0;
        return tweetContent.getUrlEntities().size();

    }

    private int calculateNumberOfReTweets(TweetContent tweetContent) {

        return Integer.parseInt(tweetContent.getRetweetCount());

    }

    private boolean doesContainsQuestionMark(String tweetText) {
        return tweetText.contains("?");
    }

    private boolean doesContainExclamtionMark(String tweetText) {
        return tweetText.contains("!");

    }
}

