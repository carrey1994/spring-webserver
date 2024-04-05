package com.jameswu.demo.service;

import com.jameswu.demo.utils.GzTexts;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HealthService {

	private final RedisService redisService;

	@Autowired
	public HealthService(RedisService redisService) {
		this.redisService = redisService;
	}

	@Transactional
	public String checkHealth() {
		String timeText = String.valueOf(Instant.now());
		redisService.setKeyValue(GzTexts.HEALTH_CHECK_ON, timeText);
		return timeText;
	}
}
