package com.fake.tweet.orchestrator;

import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

import com.fake.tweet.featuresetcreator.ContentUpdater;
import com.fake.tweet.featuresetcreator.FeatureSetCreator;
import com.fake.tweet.featuresetcreator.FeatureVectorUpdater;
import com.fake.tweet.featuresetcreator.LabelUpdater;
import com.fake.tweet.featuresetcreator.SentimentalScoreUpdater;
import com.fake.tweet.featuresetcreator.UserFeatureUpdater;
import com.fake.tweet.pojo.EvaluatorResult;
import com.fake.tweet.pojo.ExperimentOrchestratorResult;
import com.fake.tweet.pojo.FeatureSetCreatorResult;
import com.fake.tweet.pojo.TweetContentFetcherResult;
import com.fake.tweet.service.TweetCredibilityStatisticsService;
import com.fake.tweet.utils.AppConfig;

/**
 * Created by saranyakrishnan on 11/5/17.
 */

@Builder
public class ExperimentOrchestrator {
    private TweetContentFetcher twitterContentFetcher;
    private FeatureSetCreator featureSetCreator;
    private Evaluator j48Evaluator;
    private Evaluator svmEvaluator;

    public ExperimentOrchestratorResult orchestrateExperiment(List<String> tweetIds, TweetCredibilityStatisticsService tweetCredibilityStatisticsService) throws Exception {
        TweetContentFetcherResult tweetContentFetcherResult = twitterContentFetcher.fetchTweetContents(tweetIds, tweetCredibilityStatisticsService);

        FeatureSetCreatorResult featureSetCreatorResult = featureSetCreator.createFeatureSet(tweetContentFetcherResult,
                AppConfig.getConfig("TEST_DATASET") + "dataset.arff", new ArrayList<FeatureVectorUpdater>() {
                    {
                        add(new LabelUpdater());
                        add(new ContentUpdater());
                        add(new SentimentalScoreUpdater());
                        add(new UserFeatureUpdater());
                    }
                });

        EvaluatorResult j48Evaluation = j48Evaluator.evaluate(featureSetCreatorResult.getFeatureSet());
        EvaluatorResult svmEvaluation = svmEvaluator.evaluate(featureSetCreatorResult.getFeatureSet());

        return ExperimentOrchestratorResult.builder().j48Evaluation(j48Evaluation).svmEvaluation(svmEvaluation)
                .tweetIdToFeatureVectorMap(featureSetCreatorResult.getTweetIdToFeatureVector()).build();
    }
}
