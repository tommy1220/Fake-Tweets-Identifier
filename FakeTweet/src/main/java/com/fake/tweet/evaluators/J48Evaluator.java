package com.fake.tweet.evaluators;

import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;

import java.util.Random;

import com.fake.tweet.orchestrator.Evaluator;
import com.fake.tweet.pojo.EvaluatorResult;

/**
 * Created by saranyakrishnan on 11/5/17.
 */
public class J48Evaluator implements Evaluator {
    @Override
    public EvaluatorResult evaluate(Instances featureSet) throws Exception {
        J48 j48 = new J48();
        j48.setUnpruned(false);
        j48.setConfidenceFactor((float) 0.25);
        j48.setMinNumObj(2);
        j48.setBatchSize("100");
        j48.setBinarySplits(false);
        j48.setCollapseTree(true);
        j48.setNumFolds(3);
        j48.setSeed(1);
        Evaluation evaluation = new Evaluation(featureSet);
        evaluation.crossValidateModel(j48, featureSet, 10, new Random(1));
        j48.buildClassifier(featureSet);

        return EvaluatorResult.builder().classifier(j48).evaluation(evaluation).build();
    }
}
