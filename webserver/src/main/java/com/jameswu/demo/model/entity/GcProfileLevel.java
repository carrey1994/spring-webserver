package com.jameswu.demo.model.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GcProfileLevel implements Serializable {
	private int userId;
	private String email;
	private String nickName;
	private String address;
	private Instant enrollmentDate;
	private Integer recommenderId;
	private int level;

	public UserProfile toUserProfile() {
		return new UserProfile(userId, email, nickName, address, enrollmentDate, recommenderId);
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
