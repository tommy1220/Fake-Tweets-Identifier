package com.fake.tweet.pojo;

import java.util.Map;

import weka.core.Instances;

public class FeatureSetCreatorResult {

    /** The feature set itself that's used in weka for classification */
    private Instances featureSet;

    /** All the data collected to construct feature set */
    private Map<String, FeatureVector> tweetIdToFeatureVector;

    /** Errors during creation of FeatureSet */
    private String errorMessage;

    public Instances getFeatureSet() {
        return featureSet;
    }

    public void setFeatureSet(Instances featureSet) {
        this.featureSet = featureSet;
    }

    public Map<String, FeatureVector> getTweetIdToFeatureVector() {
        return tweetIdToFeatureVector;
    }

    public void setTweetIdToFeatureVector(Map<String, FeatureVector> tweetIdToFeatureVector) {
        this.tweetIdToFeatureVector = tweetIdToFeatureVector;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((errorMessage == null) ? 0 : errorMessage.hashCode());
        result = prime * result + ((featureSet == null) ? 0 : featureSet.hashCode());
        result = prime * result + ((tweetIdToFeatureVector == null) ? 0 : tweetIdToFeatureVector.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FeatureSetCreatorResult other = (FeatureSetCreatorResult) obj;
        if (errorMessage == null) {
            if (other.errorMessage != null)
                return false;
        } else if (!errorMessage.equals(other.errorMessage))
            return false;
        if (featureSet == null) {
            if (other.featureSet != null)
                return false;
        } else if (!featureSet.equals(other.featureSet))
            return false;
        if (tweetIdToFeatureVector == null) {
            if (other.tweetIdToFeatureVector != null)
                return false;
        } else if (!tweetIdToFeatureVector.equals(other.tweetIdToFeatureVector))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("FeatureSetCreatorResult [featureSet=");
        builder.append(featureSet);
        builder.append(", tweetIdToFeatureVector=");
        builder.append(tweetIdToFeatureVector);
        builder.append(", errorMessage=");
        builder.append(errorMessage);
        builder.append("]");
        return builder.toString();
    }
}
