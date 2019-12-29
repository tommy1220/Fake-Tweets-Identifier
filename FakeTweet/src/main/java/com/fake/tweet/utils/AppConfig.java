package com.fake.tweet.utils;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * Simple class that reads AppConfig.properties and provide a util
 * method to find the value of give property.
 *
 * Created by saranyakrishnan on 3/20/18.
 */
public class AppConfig {

    public static String getConfig(String configName) {
        Resource resource = new ClassPathResource("application-"+System.getProperty("spring.profiles.active")+".properties");
        Properties props = null;
        try {
            props = PropertiesLoaderUtils.loadProperties(resource);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return props.getProperty(configName);
    }
}
