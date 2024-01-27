package com.jameswu.security.demo.service;

import com.jameswu.security.demo.utils.GzTexts;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HealthService {

    @Autowired
    private RedisService redisService;

    @Transactional
    public String checkHealth() {
        String timeText = String.valueOf(Instant.now());
        redisService.setKeyValue(GzTexts.HEALTH_CHECK_ON, timeText);
        return timeText;
    }

    public String readHealth() {
        String data =
                (String) redisService.getValueByKey(GzTexts.HEALTH_CHECK_ON).get();
        return data;
    }
}
