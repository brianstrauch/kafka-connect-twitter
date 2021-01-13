# Kafka Connect Twitter

TODO: Twitter Logo and Kafka Logo

A Kafka source connector for the Twitter v2 API.

## Functionality

* Stream tweets from a @username
* Stream tweets containing a #hashtag

## Try it out!

You must obtain an API token from https://developer.twitter.com and install Confluent Platform (https://www.confluent.io/product/confluent-platform).

1. `mvn clean package`
2. `mkdir -p ${CONFLUENT_HOME}/share/java/plugins && cp target/kafka-connect-twitter-*.jar ${CONFLUENT_HOME}/share/java/plugins`
3. `confluent local services connect start`
4. `confluent local services connect connector load twitter-source -c example-config.json`
5. `confluent local services kafka consume twitter`
