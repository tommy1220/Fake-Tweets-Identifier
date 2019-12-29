package com.fake.tweet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fake.tweet.entity.TweetCredibilityStatistics;

public interface TweetCredibilityStatisticsRepository extends JpaRepository<TweetCredibilityStatistics, Long> {

    @Query(value = "select * from tweet_credibility_statistics where tweet_id = ?1", nativeQuery = true)
    TweetCredibilityStatistics getByTweetId(String tweetId);

}
