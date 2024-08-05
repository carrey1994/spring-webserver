package com.jameswu.demo.model.entity;

import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class GcProfileLevel extends UserProfile implements Serializable {
	private int level;

	public UserProfile toUserProfile() {
		return new UserProfile(
				getUserId(), getEmail(), getNickname(), getAddress(), getEnrollmentDate(), getRecommenderId());
	}

	public GcProfileLevel(UserProfile userProfile, int level) {
		super(
				userProfile.getUserId(),
				userProfile.getEmail(),
				userProfile.getNickname(),
				userProfile.getAddress(),
				userProfile.getEnrollmentDate(),
				userProfile.getRecommenderId());
		this.level = level;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		GcProfileLevel that = (GcProfileLevel) o;
		return level == that.level;
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), level);
	}
}
