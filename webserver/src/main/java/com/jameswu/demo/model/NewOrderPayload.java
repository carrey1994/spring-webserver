package com.jameswu.demo.model;

import java.io.Serializable;
import java.util.Map;

public record NewOrderPayload(Map<String, BuyingProductPayload> productIdToBuyProductPayload) implements Serializable {}
