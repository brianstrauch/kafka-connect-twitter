package com.brianstrauch.kafka.connect.twitter.model.tweets.search;

import java.util.List;

public class DeleteStreamRuleIDs {
    private List<String> ids;

    public DeleteStreamRuleIDs(List<String> ids) {
        this.ids = ids;
    }
}
