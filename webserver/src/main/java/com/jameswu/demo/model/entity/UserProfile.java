package com.jameswu.demo.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "user_profile")
@Builder
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id", nullable = false)
	private int userId;

	@Column
	@Email(message = "Invalid Email", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
	private String email;

	@Column(nullable = false)
	private String nickname;

	@Column(nullable = false)
	private String address;

	@Column(nullable = false)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
	private Instant enrollmentDate;

	/* If member joins by himself, recommenderId assigned null. */
	@Column(nullable = false)
	private int recommenderId;

	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (object == null || getClass() != object.getClass()) return false;
		UserProfile that = (UserProfile) object;
		return userId == that.userId
				&& Objects.equals(email, that.email)
				&& Objects.equals(nickname, that.nickname)
				&& Objects.equals(address, that.address)
				&& Objects.equals(enrollmentDate, that.enrollmentDate)
				&& Objects.equals(recommenderId, that.recommenderId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId, email, nickname, address, enrollmentDate, recommenderId);
	}
}
