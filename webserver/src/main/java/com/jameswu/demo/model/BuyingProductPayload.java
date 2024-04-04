package com.jameswu.demo.model;

import java.io.Serializable;
import java.util.UUID;

public record BuyingProductPayload(int quantity, UUID couponId) implements Serializable {}
