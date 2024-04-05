package com.jameswu.demo.model.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

public record LoginPayload(
		@Size(min = 8, max = 16) @NotNull String username,
		@NotNull @Size(min = 8, max = 16) String password)
		implements Serializable {}
