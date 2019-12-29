package com.fake.tweet.featuresetcreator;

import com.fake.tweet.pojo.FeatureVector;
import com.fake.tweet.pojo.TweetContent;
import com.fake.tweet.pojo.UserDetails;

/**
 * Created by saranyakrishnan on 11/7/17.
 * <p>
 * Updates the following fields in FeatureVector
 * ------------------------------------------------------------------------ 1.
 * noOfFriends; 2. noOfFollowers; 3. friendFollowerRatio; 4. noOfTimesListed; 5.
 * isUserHasURL; 6. isVerifiedUser; 7. noOfTweets;
 */
public class UserFeatureUpdater implements FeatureVectorUpdater {
    @Override
    public void updateFeatureVector(FeatureVector featureVector, TweetContent tweetContent) {
        UserDetails userDetails = tweetContent.getUserDetails();
        featureVector.setNoOfFriends(userDetails.getNoOfFriends());
        featureVector.setNoOfFollowers(userDetails.getNoOfFollowers());
        featureVector.setFriendFollowerRatio(userDetails.getFriendFollowerRatio());
        featureVector.setNoOfTimesListed(userDetails.getNoOfTimesListed());
        if (userDetails.isUserHasURL()) {
            featureVector.setIsUserHasURL(1);
        } else {
            featureVector.setIsUserHasURL(0);

        }
        if (userDetails.isVerifiedUser()) {
            featureVector.setIsVerifiedUser(1);
        } else {
            featureVector.setIsVerifiedUser(0);

        }
        featureVector.setNoOfTweets(userDetails.getNoOfTweets());

    }
}
