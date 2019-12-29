package com.fake.tweet.pojo;

public class FeatureVector {
    private String tweetID;
    // LabelUpdater columns
    private int isUrlCredible;
    private int isImageCredible;

    // ContentUpdater columns
    private String label;
    private int tweetLength;
    private int wordCount;
    private int noOfQuestionMark;
    private int noOfExclamationMark;
    private int containsQuestionMark;
    private int containsExcalmationMark;
    private int noOfUpperCaseLetter;
    private int noOfhasgTags;
    private int noOfUrls;
    private int noOfRetweets;

    // UserFeatureUpdater columns
    private int noOfFriends;
    private int noOfFollowers;
    private double friendFollowerRatio;
    private int noOfTimesListed;
    private int isUserHasURL;
    private int isVerifiedUser;
    private int noOfTweets;

    // Sentimental Analysis
    private double sentimentalScore;

    // Fields that are not part of feature set directly.
    // These fields make up few other fields described above
    private boolean doesGoogleDocumentSearchForMediaEntityHaveFakeUrl;
    private boolean doesGoogleDocumentSearchForMediaEntityHaveWordFake;
    private boolean doesGoogleImageSearchForMediaEntityHaveFakeUrl;
    private boolean doesGoogleImageSearchForMediaEntityHaveWordFakeInTitle;
    private boolean doesGoogleImageSearchForMediaEntityHaveWordFakeInDescription;
    private boolean doesGoogleImageSearchForMediaEntityHaveDateMisaligned;
    private boolean isBestGuessForGoogleImageSearchForMediaEntityUnrelated;

    private boolean containsImages;
    private boolean containsUrl;

    public String getTweetID() {
        return tweetID;
    }

    public void setTweetID(String tweetID) {
        this.tweetID = tweetID;
    }

    public int getIsUrlCredible() {
        return isUrlCredible;
    }

    public void setIsUrlCredible(int isUrlCredible) {
        this.isUrlCredible = isUrlCredible;
    }

    public int getIsImageCredible() {
        return isImageCredible;
    }

    public void setIsImageCredible(int isImageCredible) {
        this.isImageCredible = isImageCredible;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getTweetLength() {
        return tweetLength;
    }

    public void setTweetLength(int tweetLength) {
        this.tweetLength = tweetLength;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    public int getNoOfQuestionMark() {
        return noOfQuestionMark;
    }

    public void setNoOfQuestionMark(int noOfQuestionMark) {
        this.noOfQuestionMark = noOfQuestionMark;
    }

    public int getNoOfExclamationMark() {
        return noOfExclamationMark;
    }

    public void setNoOfExclamationMark(int noOfExclamationMark) {
        this.noOfExclamationMark = noOfExclamationMark;
    }

    public int getContainsQuestionMark() {
        return containsQuestionMark;
    }

    public void setContainsQuestionMark(int containsQuestionMark) {
        this.containsQuestionMark = containsQuestionMark;
    }

    public int getContainsExcalmationMark() {
        return containsExcalmationMark;
    }

    public void setContainsExcalmationMark(int containsExcalmationMark) {
        this.containsExcalmationMark = containsExcalmationMark;
    }

    public int getNoOfUpperCaseLetter() {
        return noOfUpperCaseLetter;
    }

    public void setNoOfUpperCaseLetter(int noOfUpperCaseLetter) {
        this.noOfUpperCaseLetter = noOfUpperCaseLetter;
    }

    public int getNoOfhasgTags() {
        return noOfhasgTags;
    }

    public void setNoOfhasgTags(int noOfhasgTags) {
        this.noOfhasgTags = noOfhasgTags;
    }

    public int getNoOfUrls() {
        return noOfUrls;
    }

    public void setNoOfUrls(int noOfUrls) {
        this.noOfUrls = noOfUrls;
    }

    public int getNoOfRetweets() {
        return noOfRetweets;
    }

    public void setNoOfRetweets(int noOfRetweets) {
        this.noOfRetweets = noOfRetweets;
    }

    public int getNoOfFriends() {
        return noOfFriends;
    }

    public void setNoOfFriends(int noOfFriends) {
        this.noOfFriends = noOfFriends;
    }

    public int getNoOfFollowers() {
        return noOfFollowers;
    }

    public void setNoOfFollowers(int noOfFollowers) {
        this.noOfFollowers = noOfFollowers;
    }

    public double getFriendFollowerRatio() {
        return friendFollowerRatio;
    }

    public void setFriendFollowerRatio(double friendFollowerRatio) {
        this.friendFollowerRatio = friendFollowerRatio;
    }

    public int getNoOfTimesListed() {
        return noOfTimesListed;
    }

    public void setNoOfTimesListed(int noOfTimesListed) {
        this.noOfTimesListed = noOfTimesListed;
    }

    public int getIsUserHasURL() {
        return isUserHasURL;
    }

    public void setIsUserHasURL(int isUserHasURL) {
        this.isUserHasURL = isUserHasURL;
    }

    public int getIsVerifiedUser() {
        return isVerifiedUser;
    }

    public void setIsVerifiedUser(int isVerifiedUser) {
        this.isVerifiedUser = isVerifiedUser;
    }

    public int getNoOfTweets() {
        return noOfTweets;
    }

    public void setNoOfTweets(int noOfTweets) {
        this.noOfTweets = noOfTweets;
    }

    public double getSentimentalScore() {
        return sentimentalScore;
    }

    public void setSentimentalScore(double sentimentalScore) {
        this.sentimentalScore = sentimentalScore;
    }

    public boolean isDoesGoogleDocumentSearchForMediaEntityHaveFakeUrl() {
        return doesGoogleDocumentSearchForMediaEntityHaveFakeUrl;
    }

    public void setDoesGoogleDocumentSearchForMediaEntityHaveFakeUrl(
            boolean doesGoogleDocumentSearchForMediaEntityHaveFakeUrl) {
        this.doesGoogleDocumentSearchForMediaEntityHaveFakeUrl = doesGoogleDocumentSearchForMediaEntityHaveFakeUrl;
    }

    public boolean isDoesGoogleDocumentSearchForMediaEntityHaveWordFake() {
        return doesGoogleDocumentSearchForMediaEntityHaveWordFake;
    }

    public void setDoesGoogleDocumentSearchForMediaEntityHaveWordFake(
            boolean doesGoogleDocumentSearchForMediaEntityHaveWordFake) {
        this.doesGoogleDocumentSearchForMediaEntityHaveWordFake = doesGoogleDocumentSearchForMediaEntityHaveWordFake;
    }

    public boolean isDoesGoogleImageSearchForMediaEntityHaveFakeUrl() {
        return doesGoogleImageSearchForMediaEntityHaveFakeUrl;
    }

    public void setDoesGoogleImageSearchForMediaEntityHaveFakeUrl(
            boolean doesGoogleImageSearchForMediaEntityHaveFakeUrl) {
        this.doesGoogleImageSearchForMediaEntityHaveFakeUrl = doesGoogleImageSearchForMediaEntityHaveFakeUrl;
    }

    public boolean isDoesGoogleImageSearchForMediaEntityHaveWordFakeInTitle() {
        return doesGoogleImageSearchForMediaEntityHaveWordFakeInTitle;
    }

    public void setDoesGoogleImageSearchForMediaEntityHaveWordFakeInTitle(
            boolean doesGoogleImageSearchForMediaEntityHaveWordFakeInTitle) {
        this.doesGoogleImageSearchForMediaEntityHaveWordFakeInTitle = doesGoogleImageSearchForMediaEntityHaveWordFakeInTitle;
    }

    public boolean isDoesGoogleImageSearchForMediaEntityHaveWordFakeInDescription() {
        return doesGoogleImageSearchForMediaEntityHaveWordFakeInDescription;
    }

    public void setDoesGoogleImageSearchForMediaEntityHaveWordFakeInDescription(
            boolean doesGoogleImageSearchForMediaEntityHaveWordFakeInDescription) {
        this.doesGoogleImageSearchForMediaEntityHaveWordFakeInDescription = doesGoogleImageSearchForMediaEntityHaveWordFakeInDescription;
    }

    public boolean isDoesGoogleImageSearchForMediaEntityHaveDateMisaligned() {
        return doesGoogleImageSearchForMediaEntityHaveDateMisaligned;
    }

    public void setDoesGoogleImageSearchForMediaEntityHaveDateMisaligned(
            boolean doesGoogleImageSearchForMediaEntityHaveDateMisaligned) {
        this.doesGoogleImageSearchForMediaEntityHaveDateMisaligned = doesGoogleImageSearchForMediaEntityHaveDateMisaligned;
    }

    public boolean isBestGuessForGoogleImageSearchForMediaEntityUnrelated() {
        return isBestGuessForGoogleImageSearchForMediaEntityUnrelated;
    }

    public void setBestGuessForGoogleImageSearchForMediaEntityUnrelated(
            boolean isBestGuessForGoogleImageSearchForMediaEntityUnrelated) {
        this.isBestGuessForGoogleImageSearchForMediaEntityUnrelated = isBestGuessForGoogleImageSearchForMediaEntityUnrelated;
    }

    public boolean isContainsImages() {
        return containsImages;
    }

    public void setContainsImages(boolean containsImages) {
        this.containsImages = containsImages;
    }

    public boolean isContainsUrl() {
        return containsUrl;
    }

    public void setContainsUrl(boolean containsUrl) {
        this.containsUrl = containsUrl;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + containsExcalmationMark;
        result = prime * result + (containsImages ? 1231 : 1237);
        result = prime * result + containsQuestionMark;
        result = prime * result + (containsUrl ? 1231 : 1237);
        result = prime * result + (doesGoogleDocumentSearchForMediaEntityHaveFakeUrl ? 1231 : 1237);
        result = prime * result + (doesGoogleDocumentSearchForMediaEntityHaveWordFake ? 1231 : 1237);
        result = prime * result + (doesGoogleImageSearchForMediaEntityHaveDateMisaligned ? 1231 : 1237);
        result = prime * result + (doesGoogleImageSearchForMediaEntityHaveFakeUrl ? 1231 : 1237);
        result = prime * result + (doesGoogleImageSearchForMediaEntityHaveWordFakeInDescription ? 1231 : 1237);
        result = prime * result + (doesGoogleImageSearchForMediaEntityHaveWordFakeInTitle ? 1231 : 1237);
        long temp;
        temp = Double.doubleToLongBits(friendFollowerRatio);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + (isBestGuessForGoogleImageSearchForMediaEntityUnrelated ? 1231 : 1237);
        result = prime * result + isImageCredible;
        result = prime * result + isUrlCredible;
        result = prime * result + isUserHasURL;
        result = prime * result + isVerifiedUser;
        result = prime * result + ((label == null) ? 0 : label.hashCode());
        result = prime * result + noOfExclamationMark;
        result = prime * result + noOfFollowers;
        result = prime * result + noOfFriends;
        result = prime * result + noOfQuestionMark;
        result = prime * result + noOfRetweets;
        result = prime * result + noOfTimesListed;
        result = prime * result + noOfTweets;
        result = prime * result + noOfUpperCaseLetter;
        result = prime * result + noOfUrls;
        result = prime * result + noOfhasgTags;
        temp = Double.doubleToLongBits(sentimentalScore);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((tweetID == null) ? 0 : tweetID.hashCode());
        result = prime * result + tweetLength;
        result = prime * result + wordCount;
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
        FeatureVector other = (FeatureVector) obj;
        if (containsExcalmationMark != other.containsExcalmationMark)
            return false;
        if (containsImages != other.containsImages)
            return false;
        if (containsQuestionMark != other.containsQuestionMark)
            return false;
        if (containsUrl != other.containsUrl)
            return false;
        if (doesGoogleDocumentSearchForMediaEntityHaveFakeUrl != other.doesGoogleDocumentSearchForMediaEntityHaveFakeUrl)
            return false;
        if (doesGoogleDocumentSearchForMediaEntityHaveWordFake != other.doesGoogleDocumentSearchForMediaEntityHaveWordFake)
            return false;
        if (doesGoogleImageSearchForMediaEntityHaveDateMisaligned != other.doesGoogleImageSearchForMediaEntityHaveDateMisaligned)
            return false;
        if (doesGoogleImageSearchForMediaEntityHaveFakeUrl != other.doesGoogleImageSearchForMediaEntityHaveFakeUrl)
            return false;
        if (doesGoogleImageSearchForMediaEntityHaveWordFakeInDescription != other.doesGoogleImageSearchForMediaEntityHaveWordFakeInDescription)
            return false;
        if (doesGoogleImageSearchForMediaEntityHaveWordFakeInTitle != other.doesGoogleImageSearchForMediaEntityHaveWordFakeInTitle)
            return false;
        if (Double.doubleToLongBits(friendFollowerRatio) != Double.doubleToLongBits(other.friendFollowerRatio))
            return false;
        if (isBestGuessForGoogleImageSearchForMediaEntityUnrelated != other.isBestGuessForGoogleImageSearchForMediaEntityUnrelated)
            return false;
        if (isImageCredible != other.isImageCredible)
            return false;
        if (isUrlCredible != other.isUrlCredible)
            return false;
        if (isUserHasURL != other.isUserHasURL)
            return false;
        if (isVerifiedUser != other.isVerifiedUser)
            return false;
        if (label == null) {
            if (other.label != null)
                return false;
        } else if (!label.equals(other.label))
            return false;
        if (noOfExclamationMark != other.noOfExclamationMark)
            return false;
        if (noOfFollowers != other.noOfFollowers)
            return false;
        if (noOfFriends != other.noOfFriends)
            return false;
        if (noOfQuestionMark != other.noOfQuestionMark)
            return false;
        if (noOfRetweets != other.noOfRetweets)
            return false;
        if (noOfTimesListed != other.noOfTimesListed)
            return false;
        if (noOfTweets != other.noOfTweets)
            return false;
        if (noOfUpperCaseLetter != other.noOfUpperCaseLetter)
            return false;
        if (noOfUrls != other.noOfUrls)
            return false;
        if (noOfhasgTags != other.noOfhasgTags)
            return false;
        if (Double.doubleToLongBits(sentimentalScore) != Double.doubleToLongBits(other.sentimentalScore))
            return false;
        if (tweetID == null) {
            if (other.tweetID != null)
                return false;
        } else if (!tweetID.equals(other.tweetID))
            return false;
        if (tweetLength != other.tweetLength)
            return false;
        if (wordCount != other.wordCount)
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("FeatureVector [tweetID=");
        builder.append(tweetID);
        builder.append(", isUrlCredible=");
        builder.append(isUrlCredible);
        builder.append(", isImageCredible=");
        builder.append(isImageCredible);
        builder.append(", label=");
        builder.append(label);
        builder.append(", tweetLength=");
        builder.append(tweetLength);
        builder.append(", wordCount=");
        builder.append(wordCount);
        builder.append(", noOfQuestionMark=");
        builder.append(noOfQuestionMark);
        builder.append(", noOfExclamationMark=");
        builder.append(noOfExclamationMark);
        builder.append(", containsQuestionMark=");
        builder.append(containsQuestionMark);
        builder.append(", containsExcalmationMark=");
        builder.append(containsExcalmationMark);
        builder.append(", noOfUpperCaseLetter=");
        builder.append(noOfUpperCaseLetter);
        builder.append(", noOfhasgTags=");
        builder.append(noOfhasgTags);
        builder.append(", noOfUrls=");
        builder.append(noOfUrls);
        builder.append(", noOfRetweets=");
        builder.append(noOfRetweets);
        builder.append(", noOfFriends=");
        builder.append(noOfFriends);
        builder.append(", noOfFollowers=");
        builder.append(noOfFollowers);
        builder.append(", friendFollowerRatio=");
        builder.append(friendFollowerRatio);
        builder.append(", noOfTimesListed=");
        builder.append(noOfTimesListed);
        builder.append(", isUserHasURL=");
        builder.append(isUserHasURL);
        builder.append(", isVerifiedUser=");
        builder.append(isVerifiedUser);
        builder.append(", noOfTweets=");
        builder.append(noOfTweets);
        builder.append(", sentimentalScore=");
        builder.append(sentimentalScore);
        builder.append(", doesGoogleDocumentSearchForMediaEntityHaveFakeUrl=");
        builder.append(doesGoogleDocumentSearchForMediaEntityHaveFakeUrl);
        builder.append(", doesGoogleDocumentSearchForMediaEntityHaveWordFake=");
        builder.append(doesGoogleDocumentSearchForMediaEntityHaveWordFake);
        builder.append(", doesGoogleImageSearchForMediaEntityHaveFakeUrl=");
        builder.append(doesGoogleImageSearchForMediaEntityHaveFakeUrl);
        builder.append(", doesGoogleImageSearchForMediaEntityHaveWordFakeInTitle=");
        builder.append(doesGoogleImageSearchForMediaEntityHaveWordFakeInTitle);
        builder.append(", doesGoogleImageSearchForMediaEntityHaveWordFakeInDescription=");
        builder.append(doesGoogleImageSearchForMediaEntityHaveWordFakeInDescription);
        builder.append(", doesGoogleImageSearchForMediaEntityHaveDateMisaligned=");
        builder.append(doesGoogleImageSearchForMediaEntityHaveDateMisaligned);
        builder.append(", isBestGuessForGoogleImageSearchForMediaEntityUnrelated=");
        builder.append(isBestGuessForGoogleImageSearchForMediaEntityUnrelated);
        builder.append(", containsImages=");
        builder.append(containsImages);
        builder.append(", containsUrl=");
        builder.append(containsUrl);
        builder.append("]");
        return builder.toString();
    }
}
