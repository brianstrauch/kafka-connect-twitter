package com.brianstrauch.kafka.connect.twitter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TwitterSourceTaskTest {
    @Test
    public void versionIsCorrect() {
        TwitterSourceTask task = new TwitterSourceTask();
        assertEquals(PropertiesFile.VERSION, task.version());
    }
}
