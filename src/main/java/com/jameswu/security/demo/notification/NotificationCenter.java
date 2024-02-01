package com.jameswu.security.demo.notification;

import static jodd.util.StringPool.UTF_8;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jameswu.security.demo.notification.mail.BaseMail;
import com.rabbitmq.client.Channel;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class NotificationCenter {

    @Autowired
    private List<NotificationQueue> notificationQueues;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private Channel channel;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${mail.address}")
    private String address;

    @PostConstruct
    public void execute() {
        notificationQueues.forEach(queue -> {
            try {
                channel.queueDeclare(queue.getQueueName(), false, false, false, null);
                Function<BaseMail, Boolean> fn = (email) -> {
                    startNotification(email);
                    return true;
                };
                queue.consume(fn);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    @SneakyThrows
    public void putQueue(String queue, Object clazz) {
        var notificationQueue = notificationQueues.stream()
                .filter(q -> q.getQueueName().equals(queue))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("%1s not found", queue)));
        notificationQueue.publish(clazz);
    }

    public void startNotification(BaseMail mail) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, UTF_8);
            String htmlMsg = renderedHTML(mail);
            helper.setText(htmlMsg, true); // Use this or above line.
            helper.setTo(mail.getReceiver());
            helper.setSubject(mail.getSubject());
            helper.setFrom(address);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public String renderedHTML(BaseMail mail) {
        Context ctx = new Context();
        ctx.setVariables(objectMapper.convertValue(mail, Map.class));
        return templateEngine.process(mail.getTemplate(), ctx);
    }
}
