package com.jameswu.security.demo.controller;

import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health-checker")
public class HealthController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("check")
    public String checkHealth() {
        redisTemplate.opsForValue().set("health-check-on", String.valueOf(Instant.now()));
        return HttpStatus.OK.name();
    }

    @GetMapping("read")
    public String readHealthChecker() {
        return redisTemplate.opsForValue().get("health-check-on");
    }
}
