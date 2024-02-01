package com.jameswu.security.demo.notification.mail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public abstract class BaseMail {
    String template;
    String subject;
    String receiver;
}
