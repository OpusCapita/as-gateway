package com.opuscapita.gateway.as;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class ASGateway {

    public static void main(String[] args) {
        SpringApplication.run(ASGateway.class, args);
    }

}
