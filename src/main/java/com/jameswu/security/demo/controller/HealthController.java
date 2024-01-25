package com.jameswu.security.demo.controller;

import com.jameswu.security.demo.model.Result;
import com.jameswu.security.demo.service.UserManagementService;
import com.jameswu.security.demo.utils.GzTexts;
import jakarta.transaction.Transactional;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health-checker")
public class HealthController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserManagementService userManagementService;

    @GetMapping("check")
    public Result<String> checkHealth() {
        String timeText = String.valueOf(Instant.now());
        redisTemplate.opsForValue().set(GzTexts.HEALTH_CHECK_ON, timeText);
        return new Result<>(timeText);
    }

    @GetMapping("read")
    public Result<String> readHealthChecker() {
        return new Result<>(redisTemplate.opsForValue().get(GzTexts.HEALTH_CHECK_ON));
    }

    @GetMapping("test")
    @Transactional
    public String test() {
        return "->" + userManagementService.test();
    }
}
