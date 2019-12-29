package com.fake.tweet.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * This is a single page app. This will mostly be the only model. Created by
 * saranyakrishnan on 1/8/18.
 */


public class FakeTweetIdentifierModel implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -7444520729342772656L;
    private String url;

    public FakeTweetIdentifierModel() {
    }

    @JsonCreator
    public FakeTweetIdentifierModel(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((url == null) ? 0 : url.hashCode());
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
        FakeTweetIdentifierModel other = (FakeTweetIdentifierModel) obj;
        if (url == null) {
            if (other.url != null)
                return false;
        } else if (!url.equals(other.url))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("FakeTweetIdentifierModel [url=");
        builder.append(url);
        builder.append("]");
        return builder.toString();
    }

}
