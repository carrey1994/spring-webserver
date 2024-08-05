package com.jameswu.demo.model.payload;

import com.jameswu.demo.annotation.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

public record UserProfilePayload(
		@Email @NotNull String email, @Size(max = 25, message = "Max length is 25") @NotNull String address)
		implements Serializable {}
