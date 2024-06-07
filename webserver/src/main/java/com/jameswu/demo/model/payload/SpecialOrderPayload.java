package com.jameswu.demo.model.payload;

import java.io.Serializable;

public record SpecialOrderPayload(int userId, int productId, int booked, int specialOrderId) implements Serializable {}
