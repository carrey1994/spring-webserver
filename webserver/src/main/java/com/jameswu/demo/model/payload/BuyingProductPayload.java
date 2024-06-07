package com.jameswu.demo.model.payload;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.io.Serializable;
import java.util.UUID;

public record BuyingProductPayload(@Max(value = 9999) @Min(value = 1) int quantity, @Nullable UUID couponId)
		implements Serializable {}
