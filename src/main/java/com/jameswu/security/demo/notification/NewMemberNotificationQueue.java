package com.jameswu.security.demo.notification;

import static com.jameswu.security.demo.notification.mail.QueueTag.NEW_USER_TAG;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jameswu.security.demo.model.UserProfile;
import com.jameswu.security.demo.notification.mail.BaseMail;
import com.jameswu.security.demo.notification.mail.NewMemberMail;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewMemberNotificationQueue implements NotificationQueue<UserProfile> {
    @Autowired
    private Channel channel;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public String getQueueName() {
        return NEW_USER_TAG.name();
    }

    @SneakyThrows
    @Override
    public void publish(UserProfile userProfile) {
        BaseMail mail =
                new NewMemberMail(userProfile.getUserId().toString(), userProfile.getEmail(), userProfile.getEmail());
        channel.basicPublish("", getQueueName(), null, objectMapper.writeValueAsBytes(mail));
    }

    @SneakyThrows
    @Override
    public void consume(Function<BaseMail, Boolean> fn) {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            fn.apply(objectMapper.readValue(message, NewMemberMail.class));
        };
        channel.basicConsume(getQueueName(), true, deliverCallback, consumerTag -> {});
    }
}
