package com.jameswu.demo.model.payload;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

public record OrderDetailPayload(
		@Max(value = 9999) @Min(value = 1) int quantity, @Nullable UUID couponId, @NotNull int productId)
		implements Serializable {}
