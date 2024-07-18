package com.jameswu.demo.notification;

import com.jameswu.demo.model.entity.ActiveToken;
import com.jameswu.demo.notification.mail.QueueTag;
import jakarta.persistence.PostPersist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class AuditEntityListener {

	@Autowired
	@Lazy
	private NotificationService notificationService;

	private final Logger logger = LoggerFactory.getLogger(AuditEntityListener.class);

	@PostPersist
	public void onPostPersist(ActiveToken activeToken) {
		notificationService.putQueue(QueueTag.NEW_USER_TAG, activeToken);
	}
}
