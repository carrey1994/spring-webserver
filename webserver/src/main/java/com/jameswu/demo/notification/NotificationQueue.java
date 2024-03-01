package com.jameswu.demo.notification;

import com.jameswu.demo.notification.mail.BaseMail;
import com.jameswu.demo.notification.mail.QueueTag;
import com.jameswu.demo.service.RabbitService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@Data
public abstract class NotificationQueue<T, M extends BaseMail> {

    /**
     * @Autowire failed by constructor way.
     */
    @Autowired
    private NotificationSender notificationSender;

    @Autowired
    private RabbitService rabbitService;

    //    @SneakyThrows
    public void publish(T ts) {
        rabbitService.sendMessage(queueTag().name(), formatHtml(ts));
    }

    protected void consume(M mail) {
        notificationSender.sendNotification(mail);
    }

    protected BaseMail formatHtml(T t) {
        throw new IllegalArgumentException("you need to transfer payload to html");
    }

    protected QueueTag queueTag() {
        throw new IllegalArgumentException("missed queue tag");
    }
}
