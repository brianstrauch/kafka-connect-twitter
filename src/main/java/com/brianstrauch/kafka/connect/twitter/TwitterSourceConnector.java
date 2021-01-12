package com.brianstrauch.kafka.connect.twitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.source.SourceConnector;

public class TwitterSourceConnector extends SourceConnector {
    public static final String TOPIC_CONFIG = "topic";
    public static final String BEARER_TOKEN_CONFIG = "twitter.bearer.token";
    public static final String USER_CONFIG = "twitter.user";
    public static final String HASHTAG_CONFIG = "twitter.hashtag";

    public static final ConfigDef CONFIG_DEF = new ConfigDef()
        .define(TOPIC_CONFIG, Type.STRING, Importance.HIGH, "Topic to write tweets to.")
        .define(BEARER_TOKEN_CONFIG, Type.STRING, Importance.HIGH, "Token used to access the Twitter v2 API.")
        .define(USER_CONFIG, Type.STRING, "", Importance.HIGH, "Twitter user to read tweets from.")
        .define(HASHTAG_CONFIG, Type.STRING, "", Importance.HIGH, "Hashtag to filter tweets by.");

    private Map<String, String> properties;

    @Override
    public String version() {
        return PropertiesFile.VERSION;
    }

    @Override
    public ConfigDef config() {
        return CONFIG_DEF;
    }

    @Override
    public Class<? extends Task> taskClass() {
        return TwitterSourceTask.class;
    }

    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        List<Map<String, String>> configs = new ArrayList<>();
        configs.add(properties);
        return configs;
    }

    @Override
    public void start(Map<String, String> properties) {
        this.properties = properties;
    }

    @Override
    public void stop() {}
}