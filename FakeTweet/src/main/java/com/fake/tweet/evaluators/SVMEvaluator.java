package com.fake.tweet.evaluators;

import libsvm.svm;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.core.Instances;
import weka.core.Utils;

import java.util.Random;

import com.fake.tweet.orchestrator.Evaluator;
import com.fake.tweet.pojo.EvaluatorResult;

/**
 * Created by saranyakrishnan on 11/5/17.
 */
public class SVMEvaluator implements Evaluator {
    @Override
    public EvaluatorResult evaluate(Instances featureSet) throws Exception {
        svm.svm_set_print_string_function((s) -> {
        }); // Disable console output
        SMO smoClassifier = new SMO();
        String[] options = Utils.splitOptions(
                "-C 1.0 -L 0.001 -P 1.0E-12 -N 0 -V -1 -W 1 -K \"weka.classifiers.functions.supportVector.PolyKernel -E 1.0 -C 250007\" -calibrator \"weka.classifiers.functions.Logistic -R 1.0E-8 -M -1 -num-decimal-places 4\"");
        smoClassifier.setOptions(options);
        smoClassifier.buildClassifier(featureSet);
        Evaluation evaluation = new Evaluation(featureSet);
        evaluation.crossValidateModel(smoClassifier, featureSet, 10, new Random(1));
        return EvaluatorResult.builder().classifier(smoClassifier).evaluation(evaluation).build();

    }
}
