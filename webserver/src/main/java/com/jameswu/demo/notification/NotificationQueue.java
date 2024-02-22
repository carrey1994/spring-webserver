package com.jameswu.demo.notification;

import com.jameswu.demo.notification.mail.BaseMail;
import com.jameswu.demo.notification.mail.QueueTag;
import com.jameswu.demo.service.RabbitService;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;

@Data
public abstract class NotificationQueue<M extends BaseMail> {

    /**
     * @Autowire failed by constructor way.
     */
    @Autowired
    private NotificationSender notificationSender;

    @Autowired
    private RabbitService rabbitService;

    @SneakyThrows
    public <S> void publish(S ts) {
        // TODO: S is same to T, needs a type checker here or replace S with T.
        rabbitService.sendMessage(queueTag().name(), formatHtml(ts));
    }

    protected void consume(M mail) {
        notificationSender.sendNotification(mail);
    }

    protected BaseMail formatHtml(Object t) {
        throw new IllegalArgumentException("you need to transfer payload to html");
    }

    protected QueueTag queueTag() {
        throw new IllegalArgumentException("missed queue tag");
    }
}
