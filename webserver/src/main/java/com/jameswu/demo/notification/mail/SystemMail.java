package com.jameswu.demo.notification.mail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemMail extends AbstractMail {
	private String id;
	private String name;
	private String version;

	public SystemMail(String id, String name, String receiver, String version) {
		super("system", "System notification!", receiver);
		this.id = id;
		this.name = name;
		this.version = version;
	}
}
