package com.fake.tweet.pojo;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

/**
 * Created by saranyakrishnan on 3/15/18.
 */
@Builder
@Getter
public class ExperimentOrchestratorResult {
    private EvaluatorResult j48Evaluation;
    private EvaluatorResult svmEvaluation;
    private Map<String, FeatureVector> tweetIdToFeatureVectorMap;
}
