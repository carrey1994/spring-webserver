package com.jameswu.demo.notification.mail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewMemberMail extends AbstractMail {
	private String link;
	private String nickname;

	public NewMemberMail(String link, String nickname, String receiver) {
		super("new_member", "Welcome!! Registration notification.", receiver);
		this.link = link;
		this.nickname = nickname;
	}
}
