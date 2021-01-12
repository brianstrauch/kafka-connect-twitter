package com.brianstrauch.kafka.connect.twitter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class TwitterSourceConnectorTest {
    private TwitterSourceConnector connector;

    @Before
    public void init() {
        connector = new TwitterSourceConnector();
    }

    @Test
    public void versionIsCorrect() {
        assertEquals(PropertiesFile.VERSION, connector.version());
    }

    @Test
    public void configIsNotNull() {
        assertNotNull(connector.config());
    }

    @Test 
    public void taskClassIsCorrect() {
        assertEquals(TwitterSourceTask.class, connector.taskClass());
    }
}
