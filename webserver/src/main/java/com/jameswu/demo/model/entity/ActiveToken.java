package com.jameswu.demo.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

@Entity(name = "active_token")
@Table(name = "active_token")
@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
public class ActiveToken {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(unique = true, nullable = false)
	private int id;

	/** user settings */
	@OneToOne
	@JoinColumn(name = "user_id", nullable = false)
	private GcUser user;

	@Column(name = "token", nullable = false)
	private String token;

	private ActiveToken(GcUser user) {
		this.user = user;
		this.token = createToken();
	}

	public static ActiveToken userWithDefaultToken(GcUser user) {
		return new ActiveToken(user);
	}

	private String createToken() {
		return new String(Base64.getEncoder()
				.encode(String.format(
								"%1s.%2s",
								RandomStringUtils.random(16, true, true),
								Instant.now().plus(7, ChronoUnit.DAYS).toEpochMilli())
						.getBytes()));
	}

	public boolean validateToken() {
		return Instant.ofEpochMilli(Long.parseLong(
						new String(Base64.getDecoder().decode(token.getBytes())).split("\\.")[1]))
				.isAfter(Instant.now());
	}
}
