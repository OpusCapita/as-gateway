package com.opuscapita.gateway.as.controller;

import com.opuscapita.gateway.as.BaseTest;
import kong.unirest.Unirest;
import org.junit.Test;

import static org.junit.Assert.*;

public class StatusControllerTest extends BaseTest {

    @Test
    public void shouldRespondToStatusCall() {
        String body = Unirest.get(getServerUrl() + "/status")
                .asString()
                .getBody();
        assertEquals(body, "up");
    }

}