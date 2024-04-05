package com.jameswu.demo.model.payload;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

public record ProductPayload(
		@Size(min = 1, max = 20) @NotNull String title,
		@Size(min = 1, max = 100) @NotNull String description,
		@Digits(integer = 10, fraction = 0)
				@DecimalMin(value = "0.00", message = "Price must be greater than or equal to 0.00")
				BigDecimal price,
		@Size(min = 1, max = 1000) int quantity)
		implements Serializable {}
