package com.opuscapita.gateway.as.controller;

import static org.junit.Assert.assertEquals;

import com.opuscapita.gateway.as.ASGateway;
import com.opuscapita.gateway.as.BaseControllerTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest(classes = ASGateway.class)
public class StatusControllerTest extends BaseControllerTest {
    @Override
    @Before
    public void setUp() {
        super.setUp();
    }
    @Test
    public void shouldRespondToStatusCall() throws Exception {
        String uri = "/status";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(content, "up");
    }

}