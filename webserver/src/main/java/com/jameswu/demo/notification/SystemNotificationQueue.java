package com.jameswu.demo.notification;

import com.jameswu.demo.model.entity.UserProfile;
import com.jameswu.demo.notification.mail.AbstractMail;
import com.jameswu.demo.notification.mail.QueueTag;
import com.jameswu.demo.notification.mail.SystemMail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SystemNotificationQueue extends NotificationQueue<UserProfile, SystemMail> {

    private Logger logger = LoggerFactory.getLogger(SystemNotificationQueue.class);

    @Override
    protected AbstractMail formatHtml(UserProfile t) {
        return new SystemMail(String.valueOf(t.getUserId()), t.getEmail(), t.getEmail(), "1.0.0-rc1");
    }

    @Override
    @RabbitListener(queues = "SYSTEM")
    protected void consume(SystemMail mail) {
        super.consume(mail);
    }

    @Override
    protected QueueTag queueTag() {
        return QueueTag.SYSTEM;
    }
}
