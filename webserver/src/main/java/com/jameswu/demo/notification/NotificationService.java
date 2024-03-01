package com.jameswu.demo.notification;

import com.jameswu.demo.notification.mail.BaseMail;
import com.jameswu.demo.notification.mail.QueueTag;
import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final List<NotificationQueue<?, ? extends BaseMail>> notificationQueues;
    private final Map<QueueTag, NotificationQueue<?, ? extends BaseMail>> queueMap = new HashMap<>();

    private final RabbitAdmin rabbitAdmin;

    @Autowired
    public NotificationService(
            List<NotificationQueue<?, ? extends BaseMail>> notificationQueues, RabbitAdmin rabbitAdmin) {
        this.notificationQueues = notificationQueues;
        this.rabbitAdmin = rabbitAdmin;
    }

    @PostConstruct
    public void initQueues() {
        notificationQueues.forEach(queue -> {
            rabbitAdmin.declareQueue(new Queue(queue.queueTag().name()));
            queueMap.put(queue.queueTag(), queue);
        });
    }

    @SneakyThrows
    public <T> void putQueue(QueueTag queue, T payload) {
        NotificationQueue notificationQueue = queueMap.get(queue);
        notificationQueue.publish(payload);
    }
}
