package com.brianstrauch.kafka.connect.twitter;

import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitter.dto.stream.StreamRules.StreamRule;
import com.github.redouane59.twitter.dto.tweet.Tweet;
import com.github.redouane59.twitter.signature.TwitterCredentials;

public class TwitterSourceTask extends SourceTask {
   private String topic;
   private String apiKey;
   private String apiSecretKey;
   private String user;
   private String hashtag;

   private Queue<Tweet> queue;

   @Override
   public String version() {
      return PropertiesFile.VERSION;
   }

   @Override
   public void start(Map<String, String> properties) {
      topic = properties.get(TwitterSourceConnector.TOPIC_CONFIG);
      apiKey = properties.get(TwitterSourceConnector.API_KEY_CONFIG);
      apiSecretKey = properties.get(TwitterSourceConnector.API_SECRET_KEY_CONFIG);
      user = properties.get(TwitterSourceConnector.USER_CONFIG);
      hashtag = properties.get(TwitterSourceConnector.HASHTAG_CONFIG);

      TwitterCredentials credentials = TwitterCredentials.builder()
         .apiKey(apiKey)
         .apiSecretKey(apiSecretKey)
         .build();

      TwitterClient client = new TwitterClient(credentials);

      for (StreamRule rule : client.retrieveFilteredStreamRules()) {
         client.deleteFilteredStreamRule(rule.getValue());
      }

      if (user != null) {
         client.addFilteredStreamRule(String.format("from:%s", user), null);
      }

      if (hashtag != null) {
         client.addFilteredStreamRule(String.format("#%s", hashtag), null);
      }

      queue = new LinkedList<>();
      client.startFilteredStream(tweet -> queue.add(tweet));
   }
 
   @Override
   public List<SourceRecord> poll() {
      List<SourceRecord> records = new ArrayList<>();

      while (!queue.isEmpty()) {
         Tweet tweet = queue.remove();

         records.add(new SourceRecord(
            Collections.singletonMap("tweets", tweet.getAuthorId()),
            Collections.singletonMap("timestamp", tweet.getCreatedAt().toEpochSecond(ZoneOffset.UTC)),
            topic,
            null,
            null,
            null,
            tweet.getText()
         ));
      }

      return records;
   }

   @Override
   public void stop() {}
}
