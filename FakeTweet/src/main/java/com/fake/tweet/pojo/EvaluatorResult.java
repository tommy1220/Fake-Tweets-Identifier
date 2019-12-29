package com.fake.tweet.pojo;

import lombok.Builder;
import lombok.Getter;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Evaluation;

/**
 * Created by saranyakrishnan on 3/15/18.
 */
@Builder
@Getter
public class EvaluatorResult {
    private Evaluation evaluation;
    private AbstractClassifier classifier;
}
