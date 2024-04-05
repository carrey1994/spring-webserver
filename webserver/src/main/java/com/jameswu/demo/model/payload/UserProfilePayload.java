package com.jameswu.demo.model.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

public record UserProfilePayload(
		@Email(
						message = "Invalid Email",
						regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
				@NotNull String email,
		@Size(max = 25, message = "Max length is 25") @NotNull String address)
		implements Serializable {}
