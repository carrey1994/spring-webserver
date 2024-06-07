package com.jameswu.demo.scheduled;

import com.jameswu.demo.notification.NotificationService;
import com.jameswu.demo.notification.mail.QueueTag;
import com.jameswu.demo.repository.TokenRepository;
import com.jameswu.demo.service.RedisService;
import com.jameswu.demo.utils.RedisKey;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MailScheduledService {

	private RedisService redisService;
	private TokenRepository tokenRepository;
	private NotificationService notificationService;

	@Autowired
	public MailScheduledService(
			RedisService redisService, TokenRepository tokenRepository, NotificationService notificationService) {
		this.redisService = redisService;
		this.tokenRepository = tokenRepository;
		this.notificationService = notificationService;
	}

	@Scheduled(fixedRate = 10000)
	public void sendMail() {
		var nextTime = Instant.now();
		redisService.<String>getValueByKey(RedisKey.MAIL_PREV_TIME.getKey()).ifPresent(prev -> {
			var tokens = tokenRepository.findTokenAfterLastSentTime(Instant.parse(prev), nextTime);
			tokens.forEach(token -> notificationService.putQueue(QueueTag.NEW_USER_TAG, token));
		});
		redisService.setKeyValue(RedisKey.MAIL_PREV_TIME.getKey(), String.valueOf(nextTime));
	}
}
