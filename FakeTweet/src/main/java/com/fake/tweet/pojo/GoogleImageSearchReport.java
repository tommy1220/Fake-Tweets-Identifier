package com.fake.tweet.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by saranyakrishnan on 11/26/17.
 */
@Getter
@Setter
public class GoogleImageSearchReport {
    private List<GoogleImageSearchResult> googleImageSearchResults;
    private String bestGuess;
}
