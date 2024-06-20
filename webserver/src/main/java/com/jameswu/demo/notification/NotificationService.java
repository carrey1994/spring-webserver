package com.jameswu.demo.notification;

import com.jameswu.demo.notification.mail.AbstractMail;
import com.jameswu.demo.notification.mail.QueueTag;
import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

	private final List<NotificationQueue<?, ? extends AbstractMail>> notificationQueues;
	private final Map<QueueTag, NotificationQueue<?, ? extends AbstractMail>> queueMap = new HashMap<>();
	private final Logger logger = LoggerFactory.getLogger(NotificationService.class);

	private final RabbitAdmin rabbitAdmin;

	@Autowired
	public NotificationService(
			List<NotificationQueue<?, ? extends AbstractMail>> notificationQueues, RabbitAdmin rabbitAdmin) {
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
		Optional<NotificationQueue> notificationQueue = Optional.ofNullable(queueMap.get(queue));
		notificationQueue.ifPresentOrElse(mq -> mq.publish(payload), () -> {
			logger.info("Queue not found: " + queue.name());
		});
	}
}
