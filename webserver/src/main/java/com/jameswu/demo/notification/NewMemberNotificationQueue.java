package com.jameswu.demo.notification;

import com.jameswu.demo.model.entity.UserProfile;
import com.jameswu.demo.notification.mail.AbstractMail;
import com.jameswu.demo.notification.mail.NewMemberMail;
import com.jameswu.demo.notification.mail.QueueTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NewMemberNotificationQueue extends NotificationQueue<UserProfile, NewMemberMail> {

	private Logger logger = LoggerFactory.getLogger(NewMemberNotificationQueue.class);

	@Override
	protected AbstractMail formatHtml(UserProfile t) {
		return new NewMemberMail(String.valueOf(t.getUserId()), t.getEmail(), t.getEmail());
	}

	@Override
	@RabbitListener(queues = "NEW_USER_TAG")
	protected void consume(NewMemberMail mail) {
		super.consume(mail);
	}

	@Override
	protected QueueTag queueTag() {
		return QueueTag.NEW_USER_TAG;
	}
}
