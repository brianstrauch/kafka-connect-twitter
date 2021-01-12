package com.brianstrauch.kafka.connect.twitter.model.tweets.search;

public class StreamRule {
    private String id;
    private String value;

    public StreamRule(String value) {
        this.value = value;
    }

    public String getId() {
        return id;
    }
}
