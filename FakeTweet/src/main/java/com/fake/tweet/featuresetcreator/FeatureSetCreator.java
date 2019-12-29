package com.fake.tweet.featuresetcreator;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fake.tweet.pojo.FeatureSetCreatorResult;
import com.fake.tweet.pojo.FeatureVector;
import com.fake.tweet.pojo.TweetContent;
import com.fake.tweet.pojo.TweetContentFetcherResult;

import weka.core.Instances;

@Service
public class FeatureSetCreator {
    public FeatureSetCreatorResult createFeatureSet(TweetContentFetcherResult tweetContentFetcherResult,
            String dataSetFileLocation, List<FeatureVectorUpdater> featureVectorUpdaters) throws Exception {

        Map<String, FeatureVector> tweetIdToFeatureVector = new HashMap<>();

        PrintWriter printWriter = new PrintWriter(new File(dataSetFileLocation));

        printWriter.println("@relation fakeTweetIdentifier_Dataset");
        printWriter.println();
        // printWriter.println("@attribute tweetID string");
        printWriter.println("@attribute isUrlCredible numeric");
        printWriter.println("@attribute isImageCredible numeric");
        printWriter.println("@attribute tweetLength numeric");
        printWriter.println("@attribute wordCount numeric");
        printWriter.println("@attribute noOfExclamtionMarks numeric");
        printWriter.println("@attribute noOfQuestionMarks numeric");
        printWriter.println("@attribute containsExclamationMark numeric");
        printWriter.println("@attribute containsQuestionMark numeric");
        printWriter.println("@attribute noOfHashTags numeric");
        printWriter.println("@attribute noOfUpperCaseLetters numeric");
        printWriter.println("@attribute noOfRetweets numeric");
        printWriter.println("@attribute noOfUrls numeric");
        printWriter.println("@attribute noOfFriends numeric");
        printWriter.println("@attribute noOfFollowers numeric");
        printWriter.println("@attribute friendFollowerRatio real");
        printWriter.println("@attribute noOfTimesListed numeric");
        printWriter.println("@attribute userHasUrl numeric");
        printWriter.println("@attribute isVerifiedUser numeric");
        printWriter.println("@attribute noOfTweets numeric");
        printWriter.println("@attribute sentimentalScore real");
        printWriter.println("@attribute class {fake, real}");
        printWriter.println();
        printWriter.println("@data");
        printWriter.println();

        for (TweetContent tweetContent : tweetContentFetcherResult.getTweetContents()) {
            FeatureVector featureVector = new FeatureVector();
            for (FeatureVectorUpdater featureVectorUpdater : featureVectorUpdaters) {
                featureVectorUpdater.updateFeatureVector(featureVector, tweetContent);
            }
            tweetIdToFeatureVector.put(tweetContent.getTweetId(), featureVector);
            printFeatureVector(featureVector, printWriter);
        }
        printWriter.close();
        Instances featureSet = new Instances(new InputStreamReader(new FileInputStream(dataSetFileLocation)));
        featureSet.setClassIndex(featureSet.numAttributes() - 1);

        FeatureSetCreatorResult featureSetCreatorResult = new FeatureSetCreatorResult();
        featureSetCreatorResult.setFeatureSet(featureSet);
        featureSetCreatorResult.setTweetIdToFeatureVector(tweetIdToFeatureVector);

        return featureSetCreatorResult;
    }

    private void printFeatureVector(FeatureVector featureVector, PrintWriter printWriter) {
        System.out.println("it is "+featureVector.getLabel());
        // printWriter.print(featureVector.getTweetID() + ",");
        printWriter.print(featureVector.getIsUrlCredible() + ",");
        printWriter.print(featureVector.getIsImageCredible() + ",");
        printWriter.print(featureVector.getTweetLength() + ",");
        printWriter.print(featureVector.getWordCount() + ",");
        printWriter.print(featureVector.getNoOfExclamationMark() + ",");
        printWriter.print(featureVector.getNoOfQuestionMark() + ",");
        printWriter.print(featureVector.getContainsExcalmationMark() + ",");
        printWriter.print(featureVector.getContainsQuestionMark() + ",");
        printWriter.print(featureVector.getNoOfhasgTags() + ",");
        printWriter.print(featureVector.getNoOfUpperCaseLetter() + ",");
        printWriter.print(featureVector.getNoOfRetweets() + ",");
        printWriter.print(featureVector.getNoOfUrls() + ",");
        printWriter.print(featureVector.getNoOfFriends() + ",");
        printWriter.print(featureVector.getNoOfFollowers() + ",");
        printWriter.print(featureVector.getFriendFollowerRatio() + ",");
        printWriter.print(featureVector.getNoOfTimesListed() + ",");
        printWriter.print(featureVector.getIsUserHasURL() + ",");
        printWriter.print(featureVector.getIsVerifiedUser() + ",");
        printWriter.print(featureVector.getNoOfTweets() + ",");
        printWriter.print(featureVector.getSentimentalScore() + ",");
        printWriter.print(featureVector.getLabel() + "\n");
    }
}
