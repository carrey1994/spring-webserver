package com.jameswu.security.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health-checker")
public class HealthController {

    @GetMapping
    public String healthChecker() {
        return HttpStatus.OK.name();
    }
}
