package com.jameswu.demo.model.payload;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

public record BuyingProductPayload(@Size(max = 1000) int quantity, @Nullable UUID couponId) implements Serializable {}
