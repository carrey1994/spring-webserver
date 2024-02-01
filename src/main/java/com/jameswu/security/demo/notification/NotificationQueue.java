package com.jameswu.security.demo.notification;

import com.jameswu.security.demo.notification.mail.BaseMail;
import java.util.function.Function;

public interface NotificationQueue<T> {

    String getQueueName();

    void publish(T t);

    void consume(Function<BaseMail, Boolean> fn);
}
