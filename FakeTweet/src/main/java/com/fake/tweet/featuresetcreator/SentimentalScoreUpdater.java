package com.fake.tweet.featuresetcreator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.fake.tweet.pojo.FeatureVector;
import com.fake.tweet.pojo.TweetContent;
import com.fake.tweet.utils.AppConfig;

/**
 * Created by saranyakrishnan on 11/7/17.
 */
public class SentimentalScoreUpdater implements FeatureVectorUpdater {
    @Override
    public void updateFeatureVector(FeatureVector featureVector, TweetContent tweetContent) throws IOException {
        double score =  -1;
        String tweetText = tweetContent.getTweetText();
        if(tweetText != null && !tweetText.equals("null")) {
            score = getSentimentalScore(tweetText);
        }
        featureVector.setSentimentalScore(score);
    }

    private double getSentimentalScore(String text) throws IOException {
        String url = AppConfig.getConfig("SENTIMENTAL_ANALYSIS_SERVICE") + URLEncoder.encode(text, "UTF-8");

        // HTTP GET request

            String urlObj = url;

            URL obj = new URL(urlObj);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            //con.
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println("response:" + response.toString());
             return Double.parseDouble(response.toString());
        }

    }

