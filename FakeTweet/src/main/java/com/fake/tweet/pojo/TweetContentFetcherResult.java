package com.fake.tweet.pojo;

import java.util.List;

public class TweetContentFetcherResult {
    private List<TweetContent> tweetContents;
    private String errorMessage;

    public List<TweetContent> getTweetContents() {
        return tweetContents;
    }

    public void setTweetContents(List<TweetContent> tweetContents) {
        this.tweetContents = tweetContents;
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
        result = prime * result + ((tweetContents == null) ? 0 : tweetContents.hashCode());
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
        TweetContentFetcherResult other = (TweetContentFetcherResult) obj;
        if (errorMessage == null) {
            if (other.errorMessage != null)
                return false;
        } else if (!errorMessage.equals(other.errorMessage))
            return false;
        if (tweetContents == null) {
            if (other.tweetContents != null)
                return false;
        } else if (!tweetContents.equals(other.tweetContents))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TweetContentFetcherResult [tweetContents=");
        builder.append(tweetContents);
        builder.append(", errorMessage=");
        builder.append(errorMessage);
        builder.append("]");
        return builder.toString();
    }
}
