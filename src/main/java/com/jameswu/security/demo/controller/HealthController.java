package com.jameswu.security.demo.controller;

import com.jameswu.security.demo.model.Result;
import com.jameswu.security.demo.service.RedisService;
import com.jameswu.security.demo.service.UserManagementService;
import com.jameswu.security.demo.utils.GzTexts;
import java.time.Instant;
import java.time.LocalDate;
import org.apache.log4j.Logger;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health-checker")
public class HealthController {

    @Autowired
    private static final Logger logger = Logger.getLogger(HealthController.class.getName());

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserManagementService userManagementService;

    @GetMapping("check")
    public Result<String> checkHealth() {
        String timeText = String.valueOf(Instant.now());
        logger.info(String.format("%1s -> %2s", GzTexts.HEALTH_CHECK_ON, timeText));
        return new Result<>(timeText);
    }

    @GetMapping("read")
    public Result<String> readHealthChecker() {
        return new Result<>("redisTemplate.opsForValue().get(GzTexts.HEALTH_CHECK_ON)");
    }

    @GetMapping("test")
    public String test() {
        RLock rLock = redisService.tryLock();
        int value = userManagementService.test();
        rLock.unlock();
        logger.info(String.format("%1s -> %2s", LocalDate.now(), "->  " + value));
        return "->" + value;
    }
}
