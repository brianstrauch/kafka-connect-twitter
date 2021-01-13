# Kafka Connect Twitter

A Kafka source connector for the Twitter v2 API.

<div>
  <img src="https://github.com/brianstrauch/kafka-connect-twitter/blob/master/images/kafka.png" width=100 />
  <img src="https://github.com/brianstrauch/kafka-connect-twitter/blob/master/images/twitter.png" width=170 />
</div>

## Functionality

* <a href="https://github.com/brianstrauch/kafka-connect-twitter/blob/master/examples/source-config-user.json">Stream tweets from a @user</a>
* <a href="https://github.com/brianstrauch/kafka-connect-twitter/blob/master/examples/source-config-hashtag.json">Stream tweets containing a #hashtag</a>

## Try it out!

You must first obtain an API key from https://developer.twitter.com and install Confluent Platform from https://www.confluent.io/product/confluent-platform.

1. `mvn clean package`
2. `mkdir -p ${CONFLUENT_HOME}/share/java/plugins && cp target/kafka-connect-twitter-*.jar ${CONFLUENT_HOME}/share/java/plugins`
3. `confluent local services connect start`
4. `confluent local services connect connector load twitter-source -c examples/source-config-hashtag.json`
5. `confluent local services kafka consume twitter`

## ⚠️ Under Development ⚠️

Issues and pull requests are welcome!
