package com.jameswu.security.demo.notification.mail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewMemberMail extends BaseMail {
    private String id;
    private String name;

    public NewMemberMail(String id, String name, String receiver) {
        super("new_member", "We have new member!", receiver);
        this.id = id;
        this.name = name;
    }
}
