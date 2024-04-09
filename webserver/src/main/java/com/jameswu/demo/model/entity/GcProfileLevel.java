package com.jameswu.demo.model.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GcProfileLevel extends UserProfile implements Serializable {
	private int level;

	public UserProfile toUserProfile() {
		return new UserProfile(
				getUserId(),
				getEmail(),
				getNickname(),
				getAddress(),
				getEnrollmentDate(),
				getRecommenderId());
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
}
