package com.brianstrauch.kafka.connect.twitter;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesFile {
    public static final String VERSION = extract("version");

    private static String extract(String property) {
        try (InputStream stream = PropertiesFile.class.getResourceAsStream("/kafka-connect-twitter.properties")) {
            Properties properties = new Properties();
            properties.load(stream);
            return properties.getProperty(property, null);
        } catch (Exception e) {
            return null;
        }
    }
}
