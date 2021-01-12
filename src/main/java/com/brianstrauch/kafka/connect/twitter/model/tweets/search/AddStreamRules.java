package com.brianstrauch.kafka.connect.twitter.model.tweets.search;

import java.util.List;

public class AddStreamRules {
    List<StreamRule> add;

    public AddStreamRules(List<StreamRule> add) {
        this.add = add;
    }
}
