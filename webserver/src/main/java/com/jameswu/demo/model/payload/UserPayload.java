package com.jameswu.demo.model.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

public record UserPayload(
		@Size(min = 8, max = 16) @NotNull String username,
		@Size(min = 8, max = 16) @NotNull String password,
		@Email(
						message = "Invalid Email",
						regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
				@NotNull String email,
		@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC") @NotNull Instant date,
		@Size(max = 25) @NotNull String address,
		@Nullable Integer recommenderId)
		implements Serializable {}
