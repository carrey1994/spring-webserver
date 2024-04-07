package com.jameswu.demo.notification;

import static jodd.util.StringPool.UTF_8;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jameswu.demo.notification.mail.AbstractMail;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class NotificationSender {

	private Logger logger = LoggerFactory.getLogger(NotificationSender.class);

	@Autowired
	public NotificationSender(
			JavaMailSender javaMailSender,
			SpringTemplateEngine templateEngine,
			ObjectMapper objectMapper) {
		this.javaMailSender = javaMailSender;
		this.templateEngine = templateEngine;
		this.objectMapper = objectMapper;
	}

	private final JavaMailSender javaMailSender;
	private final SpringTemplateEngine templateEngine;
	private final ObjectMapper objectMapper;

	@Value("${mail.address}")
	private String address;

	@Async
	public void sendNotification(AbstractMail mail) {
		try {
			logger.info(String.format("ID -> %s", Thread.currentThread().getId()));
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, UTF_8);
			String htmlMsg = renderedHTML(mail);
			helper.setText(htmlMsg, true);
			helper.setTo(mail.getReceiver());
			helper.setSubject(mail.getSubject());
			helper.setFrom(address);
			javaMailSender.send(mimeMessage);
		} catch (MessagingException e) {
			logger.info("Sending message failed.");
		}
	}

	public String renderedHTML(AbstractMail mail) {
		Context ctx = new Context();
		ctx.setVariables(objectMapper.convertValue(mail, Map.class));
		return templateEngine.process(mail.getTemplate(), ctx);
	}
}
