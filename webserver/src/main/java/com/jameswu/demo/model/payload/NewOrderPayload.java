package com.jameswu.demo.model.payload;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;

public record NewOrderPayload(@NotNull Map<String, BuyingProductPayload> productIdToBuyProductPayload)
        implements Serializable {}
