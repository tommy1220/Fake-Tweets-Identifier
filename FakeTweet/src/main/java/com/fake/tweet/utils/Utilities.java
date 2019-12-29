package com.fake.tweet.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by saranyakrishnan on 11/26/17.
 */
public class Utilities {

    //Read contents of file and return the content as String
    public  static String readContentFromFile(String fileName, String folderName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(folderName + "/" + fileName + ".txt"));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }
    }

    //Check if the Tweet content file is present in the existing dataset
    public static boolean isFileExists(String file, String folder) {
        File f = new File(folder + "/" + file + ".txt");
        return f.exists();
    }

    public static Date convertTweetStringToDate(String dateString) throws ParseException {
        //"Oct 31, 2012 5:00:24 PM"
        DateFormat format = new SimpleDateFormat("MMM d, yyyy h:m:s a", Locale.ENGLISH);
        if(dateString != null && !dateString.equals("")) {
            Date date = format.parse(dateString);
            return date;
        }
        return null;
    }

    public static Date convertGoogleStringToDate(String dateString) throws ParseException {
        // Nov 5, 2012
        try {
            dateString = dateString.replaceAll("\"", "");
            dateString = dateString.trim();
            DateFormat format = new SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH);
            if(dateString != null && !dateString.equals("")) {
                Date date = format.parse(dateString);
                return date;
            }
        }
        catch (ParseException exception) {
            return null;
        }

        return null;
    }

    public static Boolean compareDate(Date tweetCreatedDate, Date googleTaggedDate) {
        if (tweetCreatedDate.compareTo(googleTaggedDate) > 0) {
            Calendar myCal = Calendar.getInstance();
            myCal.setTime(googleTaggedDate);
            myCal.add(Calendar.YEAR, 1);
            if (tweetCreatedDate.before(myCal.getTime()))
                return true;
            else
                return false;
        } else if (tweetCreatedDate.compareTo(googleTaggedDate) < 0) {
            return true;
        } else if (tweetCreatedDate.compareTo(googleTaggedDate) == 0) {
            return true;
        }
        return false;
    }
}
