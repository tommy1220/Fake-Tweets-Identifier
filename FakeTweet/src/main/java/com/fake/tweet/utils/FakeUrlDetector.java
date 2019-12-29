package com.fake.tweet.utils;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;

/**
 * Created by saranyakrishnan on 11/26/17.
 */
public class FakeUrlDetector {

    private static List<String> fakeWebSites;

    static {
        // Read from fake.csv and load it in memory.
        try {
            fakeWebSites =  readFakeURLSource(AppConfig.getConfig("FAKE_URL_SOURCE"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isUrlInFakeWebsitesList(String url) throws MalformedURLException {
        // Answer if url is there in fakeWebsites List
        try {
            String tweetURL = null;
            if(!url.startsWith("https:")) {
                tweetURL = "https://" + url;
            } else {
                tweetURL = url;
            }
            URL lURL = new URL(tweetURL);
            String hostName = lURL.getHost();
            String[] fakeWebsitesArray = hostName.split(".");
            if(fakeWebsitesArray.length > 0) {
                if (containsIgnoreCase(fakeWebsitesArray[0], hostName)) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            System.out.println("url = [" + url + "] had problems while checking its presense in fake website list. Maybe URL format is wrong. See ");
            // e.printStackTrace();
        }

        return false;

    }

    private static List<String> readFakeURLSource(String fileName) throws IOException {
        ArrayList<String> fakeURLList = new ArrayList<>();
        try (InputStream ExcelFileToRead = new FileInputStream(fileName)) {
            XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);
            XSSFSheet sheet = wb.getSheetAt(0);
            XSSFRow row;
            Iterator rows = sheet.rowIterator();
            while (rows.hasNext()) {
                row = (XSSFRow) rows.next();
                XSSFCell siteURLCell = row.getCell(8);
                if (siteURLCell != null && siteURLCell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
                    if (!fakeURLList.contains(siteURLCell.toString())) {
                        fakeURLList.add(siteURLCell.toString());
                    }
                }
            }
        }
        return fakeURLList;
    }


}

