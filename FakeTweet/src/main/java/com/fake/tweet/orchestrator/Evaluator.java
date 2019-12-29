package com.fake.tweet.orchestrator;

import com.fake.tweet.pojo.EvaluatorResult;

import weka.core.Instances;

/**
 * Created by saranyakrishnan on 11/5/17.
 */
public interface Evaluator {
    EvaluatorResult evaluate(Instances featureSet) throws Exception;
}
