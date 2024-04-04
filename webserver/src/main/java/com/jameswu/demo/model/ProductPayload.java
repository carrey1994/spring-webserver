package com.jameswu.demo.model;

import java.io.Serializable;
import java.math.BigDecimal;

public record ProductPayload(String title, String description, BigDecimal price, int quantity)
        implements Serializable {}
