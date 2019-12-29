package com.fake.tweet.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by saranyakrishnan on 3/25/18.
 */

@Entity
@Table(name = "tweet_credibility_statistics")
public class TweetCredibilityStatistics implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tweet_id")
    private String tweetId;

    @Column(name = "real_count")
    private Long realCount;

    @Column(name = "fake_count")
    private Long fakeCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTweetId() {
        return tweetId;
    }

    public void setTweetId(String tweetId) {
        this.tweetId = tweetId;
    }

    public Long getRealCount() {
        return realCount;
    }

    public void setRealCount(Long realCount) {
        this.realCount = realCount;
    }

    public Long getFakeCount() {
        return fakeCount;
    }

    public void setFakeCount(Long fakeCount) {
        this.fakeCount = fakeCount;
    }
}
