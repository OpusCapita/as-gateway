package com.opuscapita.gateway.as;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ASGatewayTests {

    @MockBean
    private DataSource dataSource;

    @Test
    public void contextLoads() {
    }

}

