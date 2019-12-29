package com.fake.tweet.featuresetcreator;

import com.fake.tweet.pojo.FeatureVector;
import com.fake.tweet.pojo.TweetContent;

/**
 * Created by saranyakrishnan on 11/7/17.
 */
public interface FeatureVectorUpdater {
    void updateFeatureVector(FeatureVector featureVector, TweetContent tweetContent) throws Exception;
}
