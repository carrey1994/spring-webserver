package com.jameswu.demo.notification.mail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public abstract class AbstractMail {
    String template;
    String subject;
    String receiver;
}
