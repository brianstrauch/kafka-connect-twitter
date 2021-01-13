# Kafka Connect Twitter

A Kafka source connector for the Twitter v2 API.

<div>
  <img src="https://github.com/brianstrauch/kafka-connect-twitter/blob/master/images/kafka.png" width=100 />
  <img src="https://github.com/brianstrauch/kafka-connect-twitter/blob/master/images/twitter.png" width=170 />
</div>

## Functionality

* Stream tweets from a @username
* Stream tweets containing a #hashtag

## Try it out!

You must first obtain an API token from https://developer.twitter.com and install Confluent Platform from https://www.confluent.io/product/confluent-platform.

1. `mvn clean package`
2. `mkdir -p ${CONFLUENT_HOME}/share/java/plugins && cp target/kafka-connect-twitter-*.jar ${CONFLUENT_HOME}/share/java/plugins`
3. `confluent local services connect start`
4. `confluent local services connect connector load twitter-source -c example-config.json`
5. `confluent local services kafka consume twitter`

## ⚠️ Under Development ⚠️

Issues and pull requests are welcome!
