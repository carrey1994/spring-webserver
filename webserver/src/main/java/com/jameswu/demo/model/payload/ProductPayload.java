package com.jameswu.demo.model.payload;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

public record ProductPayload(
		@Size(min = 1, max = 20) @NotNull String title,
		@Size(min = 1, max = 100) @NotNull String description,
		@Digits(integer = 10, fraction = 0)
				@DecimalMin(value = "0.00", message = "Price must be greater than or equal to 0.00")
				@DecimalMax(
						value = "999999999.999",
						message = "Price must be less than or equal to 999999999999999.99")
				BigDecimal price,
		@Min(value = 1) @Max(value = 1000) int quantity)
		implements Serializable {}
