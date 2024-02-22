package com.jameswu.demo.notification;

import com.jameswu.demo.model.entity.UserProfile;
import com.jameswu.demo.notification.mail.BaseMail;
import com.jameswu.demo.notification.mail.QueueTag;
import com.jameswu.demo.notification.mail.SystemMail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SystemNotificationQueue extends NotificationQueue<SystemMail> {

    private Logger logger = LoggerFactory.getLogger(SystemNotificationQueue.class);

    @Override
    protected BaseMail formatHtml(Object t) {
        if (t instanceof UserProfile userProfile) {
            return new SystemMail(
                    String.valueOf(userProfile.getUserId()),
                    userProfile.getEmail(),
                    userProfile.getEmail(),
                    "1.0.0-rc1");
        }
        throw new IllegalArgumentException("Wrong payload class in SystemNotificationQueue.");
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
