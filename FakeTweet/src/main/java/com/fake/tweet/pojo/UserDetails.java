package com.fake.tweet.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by saranyakrishnan on 11/26/17.
 */

@Getter
@Setter
public class UserDetails {

    private int noOfFriends;
    private int noOfFollowers;
    private double friendFollowerRatio;
    private int noOfTimesListed;
    private boolean isUserHasURL;
    private boolean isVerifiedUser;
    private int noOfTweets;
    private String userID;
    private String userName;
}
