package com.jameswu.demo.model.entity;

import static com.jameswu.demo.utils.GzTexts.DEFAULT_RECOMMENDER_ID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jameswu.demo.annotation.Email;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

	@Email
	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "nickname", nullable = false)
	private String nickname;

	@Column(name = "address", nullable = false)
	private String address;

	@Column(name = "enrollment_date", nullable = false)
	private Instant enrollmentDate;

	/* The recommenderId assigned zero means no any recommender. */
	@Column(name = "recommender_id", nullable = false)
	private int recommenderId;

	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (object == null || getClass() != object.getClass()) return false;
		UserProfile that = (UserProfile) object;
		return userId == that.userId
				&& recommenderId == that.recommenderId
				&& Objects.equals(email, that.email)
				&& Objects.equals(nickname, that.nickname)
				&& Objects.equals(address, that.address)
				&& Objects.equals(enrollmentDate, that.enrollmentDate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId, email, nickname, address, enrollmentDate, recommenderId);
	}

	@JsonIgnore
	public boolean isRecommenderExists() {
		return recommenderId != DEFAULT_RECOMMENDER_ID;
	}
}
