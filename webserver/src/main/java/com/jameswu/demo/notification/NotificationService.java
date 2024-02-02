package com.jameswu.demo.notification;

import com.jameswu.demo.notification.mail.BaseMail;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.SneakyThrows;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final List<NotificationQueue<?, ? extends BaseMail>> notificationQueues;

    private final RabbitAdmin rabbitAdmin;

    @Autowired
    public NotificationService(
            List<NotificationQueue<?, ? extends BaseMail>> notificationQueues, RabbitAdmin rabbitAdmin) {
        this.notificationQueues = notificationQueues;
        this.rabbitAdmin = rabbitAdmin;
    }

    @PostConstruct
    public void initQueues() {
        notificationQueues.forEach(
                queue -> rabbitAdmin.declareQueue(new Queue(queue.queueTag().name())));
    }

    @SneakyThrows
    public <T> void putQueue(String queue, T payload) {
        notificationQueues.stream()
                .filter(q -> q.queueTag().name().equals(queue))
                .findFirst()
                .ifPresentOrElse(q -> q.publish(payload), () -> {
                    throw new IllegalArgumentException(String.format("%1s not found", queue));
                });
    }
}
