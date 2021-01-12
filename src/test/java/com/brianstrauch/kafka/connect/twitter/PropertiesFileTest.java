package com.brianstrauch.kafka.connect.twitter;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class PropertiesFileTest {
    @Test
    public void versionIsNotEmpty() {
        assertNotNull(PropertiesFile.VERSION);
    }
}
