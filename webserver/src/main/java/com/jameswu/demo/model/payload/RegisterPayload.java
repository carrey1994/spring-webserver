package com.jameswu.demo.model.payload;

import static com.jameswu.demo.utils.GzTexts.DEFAULT_RECOMMENDER_ID;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

public record RegisterPayload(
		@Size(min = 8, max = 16) @NotNull String username,
		@Size(min = 8, max = 16) @NotNull String password,
		@Size(max = 16) @NotNull String nickname,
		@Email(
						message = "Invalid Email",
						regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
				@NotNull String email,
		@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC") @NotNull Instant date,
		@Size(max = 25) @NotNull String address,
		@Min(0) int recommenderId)
		implements Serializable {

	public boolean isRecommenderExists() {
		return recommenderId != DEFAULT_RECOMMENDER_ID;
	}
}
