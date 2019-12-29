package com.fake.tweet.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by saranyakrishnan on 11/5/17.
 */

@Getter
@Setter
public class TweetContent {
    // This is a pojo. Add all necessary fields here
    // Use lombok @Getter, @Setter, @Builder everywhere


    private String tweetId;
    private String tweetLang;
    private String createdDate;
    private List<String> mediaEntities;
    private List<String> urlEntities;
    private List<String> hashtagEntities;
    private String tweetText;
    private String retweetCount;
    private UserDetails userDetails;
    private String label;
}
