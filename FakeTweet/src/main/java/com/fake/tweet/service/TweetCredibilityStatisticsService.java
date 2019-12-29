package com.fake.tweet.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fake.tweet.entity.TweetCredibilityStatistics;
import com.fake.tweet.repository.TweetCredibilityStatisticsRepository;

/**
 * Created by saranyakrishnan on 3/25/18.
 */
@Service
public class TweetCredibilityStatisticsService {

    @Autowired
    private TweetCredibilityStatisticsRepository repository;

    public TweetCredibilityStatistics getTweetCredibilityStatistics(final String tweetId) {
        return repository.getByTweetId(tweetId);
    }

    public void saveTweetCredibilityStatistics(String tweetId, String vote) {
        TweetCredibilityStatistics tweetCredibilityStatistics = repository.getByTweetId(tweetId);
        if (tweetCredibilityStatistics == null) {
            tweetCredibilityStatistics = new TweetCredibilityStatistics();
            tweetCredibilityStatistics.setTweetId(tweetId);
            tweetCredibilityStatistics.setFakeCount(0L);
            tweetCredibilityStatistics.setRealCount(0L);
        }
        updateTweetCredibilityStatisticsBasedOnVote(tweetCredibilityStatistics, vote);
        repository.save(tweetCredibilityStatistics);
    }
    
    public void saveTweetCredibilityStatistics(String tweetId) {
        TweetCredibilityStatistics tweetCredibilityStatistics = repository.getByTweetId(tweetId);
        if (tweetCredibilityStatistics == null) {
            tweetCredibilityStatistics = new TweetCredibilityStatistics();
            tweetCredibilityStatistics.setTweetId(tweetId);
            tweetCredibilityStatistics.setFakeCount(0L);
            tweetCredibilityStatistics.setRealCount(0L);
        }
        repository.save(tweetCredibilityStatistics);
    }

    private void updateTweetCredibilityStatisticsBasedOnVote(TweetCredibilityStatistics tweetCredibilityStatistics,
            String vote) {
        if (vote.equals("REAL")) {
            tweetCredibilityStatistics.setRealCount(tweetCredibilityStatistics.getRealCount() + 1);
        } else {
            tweetCredibilityStatistics.setFakeCount(tweetCredibilityStatistics.getFakeCount() + 1);
        }
    }

    public List<String> getAllTweetIds() {
        List<String> tweetdIds = new ArrayList<>();
        for (TweetCredibilityStatistics tweetCredibilityStatistics : repository.findAll()) {
            tweetdIds.add(tweetCredibilityStatistics.getTweetId());
        }
        return tweetdIds;
    }
}
