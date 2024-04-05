package com.jameswu.demo.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GcProfileLevel implements Serializable {
	private int userId;
	private String email;
	private String address;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
	private Instant enrollmentDate;

	private Integer recommenderId;
	private int level;

	public UserProfile toUserProfile() {
		return new UserProfile(userId, email, address, enrollmentDate, recommenderId);
	}
}
