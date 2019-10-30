package com.opuscapita.gateway.as;

import com.helger.as2servlet.AS2WebAppListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ASGateway {

    public static void main(String[] args) {
        SpringApplication.run(ASGateway.class, args);
    }

}
