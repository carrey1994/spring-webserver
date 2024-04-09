package com.jameswu.demo.notification;

import com.jameswu.demo.model.entity.ActiveToken;
import com.jameswu.demo.notification.mail.AbstractMail;
import com.jameswu.demo.notification.mail.NewMemberMail;
import com.jameswu.demo.notification.mail.QueueTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NewMemberNotificationQueue extends NotificationQueue<ActiveToken, NewMemberMail> {

	private Logger logger = LoggerFactory.getLogger(NewMemberNotificationQueue.class);

	@Override
	protected AbstractMail formatHtml(ActiveToken t) {
		return new NewMemberMail(
				"localhost:8080/api/v1/public/activate?token=" + t.getToken(),
				t.getUser().getProfile().getNickname(),
				t.getUser().getProfile().getEmail());
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
