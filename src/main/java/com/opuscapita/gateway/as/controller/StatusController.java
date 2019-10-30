package com.opuscapita.gateway.as.controller;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ConditionalOnProperty("test")
public class StatusController {

    @RequestMapping(value = {"/status"})
    public String status() {
        return "up";
    }

}
