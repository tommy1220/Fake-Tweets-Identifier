package com.fake.tweet.featuresetcreator;

import com.fake.tweet.pojo.FeatureVector;
import com.fake.tweet.pojo.TweetContent;

public class UnknownClassUpdater implements FeatureVectorUpdater {

    @Override
    public void updateFeatureVector(FeatureVector featureVector, TweetContent tweetContent) throws Exception {
        featureVector.setLabel("?");
    }
}
