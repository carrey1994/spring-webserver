package com.jameswu.demo.notification;

import com.jameswu.demo.model.entity.UserProfile;
import com.jameswu.demo.notification.mail.BaseMail;
import com.jameswu.demo.notification.mail.NewMemberMail;
import com.jameswu.demo.notification.mail.QueueTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NewMemberNotificationQueue extends NotificationQueue<NewMemberMail> {

    private Logger logger = LoggerFactory.getLogger(NewMemberNotificationQueue.class);

    @Override
    protected BaseMail formatHtml(Object t) {
        if (t instanceof UserProfile userProfile) {
            return new NewMemberMail(
                    String.valueOf(userProfile.getUserId()), userProfile.getEmail(), userProfile.getEmail());
        }
        throw new IllegalArgumentException("Wrong payload class in NewMemberNotificationQueue.");
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
