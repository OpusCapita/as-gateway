package com.opuscapita.gateway.as;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public abstract class BaseTest {

    @Value("${server.scheme:http}")
    protected String SERVER_SCHEME;

    @Value("${server.host:localhost}")
    protected String SERVER_HOST;

    @Value("${server.port:3056}")
    protected int SERVER_PORT;

    protected String getServerUrl() {
        return String.format("%s://%s:%d", SERVER_SCHEME, SERVER_HOST, SERVER_PORT);
    }

}
