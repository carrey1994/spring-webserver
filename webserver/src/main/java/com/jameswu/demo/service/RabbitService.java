package com.jameswu.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jameswu.demo.model.payload.SpecialOrderPayload;
import com.jameswu.demo.notification.mail.AbstractMail;
import com.jameswu.demo.notification.mail.QueueTag;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitService {

	private final Logger logger = LoggerFactory.getLogger(RabbitService.class);
	private final RabbitTemplate rabbitTemplate;
	private final ObjectMapper objectMapper;
	private final MessageProperties messageProperties;

	@Autowired
	public RabbitService(
			RabbitTemplate rabbitTemplate, ObjectMapper objectMapper, MessageProperties messageProperties) {
		this.rabbitTemplate = rabbitTemplate;
		this.objectMapper = objectMapper;
		this.messageProperties = messageProperties;
	}

	@SneakyThrows
	public void sendEmail(String queueTagName, AbstractMail payload) {
		Message message = new Message(objectMapper.writeValueAsBytes(payload), messageProperties);
		rabbitTemplate.send(queueTagName, message);
	}

	@SneakyThrows
	public void sendSpecialsOrder(SpecialOrderPayload specialOrderPayload) {
		Message message = new Message(objectMapper.writeValueAsBytes(specialOrderPayload), messageProperties);
		rabbitTemplate.send(QueueTag.SPECIAL_ORDER.name(), message);
	}
}
