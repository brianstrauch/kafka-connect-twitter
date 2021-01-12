package com.brianstrauch.kafka.connect.twitter;

import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.brianstrauch.kafka.connect.twitter.model.tweets.Tweet;
import com.brianstrauch.kafka.connect.twitter.model.tweets.search.AddStreamRules;
import com.brianstrauch.kafka.connect.twitter.model.tweets.search.DeleteStreamRule;
import com.brianstrauch.kafka.connect.twitter.model.tweets.search.DeleteStreamRuleIDs;
import com.brianstrauch.kafka.connect.twitter.model.tweets.search.StreamRule;
import com.brianstrauch.kafka.connect.twitter.model.tweets.search.StreamRules;
import com.google.gson.Gson;

public class TwitterSourceTask extends SourceTask {
   private static final String BASE_URL = "https://api.twitter.com/2";

   private String topic;
   private String bearerToken;
   private String user;
   private String hashtag;

   private Gson gson;
   private Scanner scanner;

   @Override
   public String version() {
      return PropertiesFile.VERSION;
   }

   @Override
   public void start(Map<String, String> properties) {
      topic = properties.get(TwitterSourceConnector.TOPIC_CONFIG);
      bearerToken = properties.get(TwitterSourceConnector.BEARER_TOKEN_CONFIG);
      user = properties.get(TwitterSourceConnector.USER_CONFIG);
      hashtag = properties.get(TwitterSourceConnector.HASHTAG_CONFIG);

      gson = new Gson();

      List<StreamRule> rules = new ArrayList<>();
      if (user != "") {
         rules.add(new StreamRule(String.format("from:%s", user)));
      }
      if (hashtag != "") {
         rules.add(new StreamRule(String.format("#%s", hashtag)));
      }

      try {
         updateStreamRules(rules);
         scanner = getStream("/tweets/search/stream");
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   @Override
   public List<SourceRecord> poll() {
      List<SourceRecord> records = new ArrayList<>();

      if (scanner.hasNextLine()) {
         String line = scanner.nextLine();

         if (line.length() > 0) {
            Tweet tweet = gson.fromJson(line, Tweet.class);

            records.add(new SourceRecord(
               Collections.singletonMap("tweets", user),
               Collections.singletonMap("timestamp", 0),
               topic,
               null,
               null,
               null,
               tweet.getData().getText()
            ));
         }
      }

      return records;
   }

   @Override
   public void stop() {
      scanner.close();
   }

   private void updateStreamRules(List<StreamRule> rules) throws Exception {
      // Get old rules
      String json = get("/tweets/search/stream/rules");
      List<StreamRule> oldRules = gson.fromJson(json, StreamRules.class).getData();

      List<String> ids = oldRules.stream()
         .map(rule -> rule.getId())
         .collect(Collectors.toList());

      // Delete old rules
      post("/tweets/search/stream/rules", new DeleteStreamRule(new DeleteStreamRuleIDs(ids)));

      // Add new rules
      post("/tweets/search/stream/rules", new AddStreamRules(rules));
   }

   private String get(String endpoint) throws Exception {
      return request(endpoint, "GET", BodyPublishers.noBody());
   }

   private String post(String endpoint, Object o) throws Exception {
      return request(endpoint, "POST", BodyPublishers.ofString(gson.toJson(o)));
   }

   private String request(String endpoint, String method, BodyPublisher body) throws Exception {
      HttpRequest req = HttpRequest.newBuilder()
         .method(method, body)
         .uri(new URI(BASE_URL + endpoint))
         .header("Authorization", String.format("Bearer %s", bearerToken))
         .header("Content-Type", "application/json")
         .build();

      HttpClient client = HttpClient.newHttpClient();
      HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
      return res.body();
   }

   private Scanner getStream(String endpoint) throws IOException {
      URL url = new URL(BASE_URL + endpoint);

      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestProperty("Authorization", String.format("Bearer %s", bearerToken));
      conn.setRequestProperty("Content-Type", "application/json");

      return new Scanner(conn.getInputStream());
   }
}
