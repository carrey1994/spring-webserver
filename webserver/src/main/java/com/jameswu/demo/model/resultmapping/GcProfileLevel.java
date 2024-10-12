package com.jameswu.demo.model.resultmapping;

import com.jameswu.demo.model.entity.UserProfile;
import java.io.Serializable;
import java.time.Instant;

public record GcProfileLevel(
		int userId,
		String email,
		String nickName,
		String address,
		Instant enrollmentDate,
		Integer recommenderId,
		int level)
		implements Serializable {
	public UserProfile toUserProfile() {
		return new UserProfile(userId, email, nickName, address, enrollmentDate, recommenderId);
	}
}
