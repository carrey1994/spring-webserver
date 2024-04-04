package com.jameswu.demo.model;

import java.io.Serializable;

public record NewOrderPayload(long productId, long userId) implements Serializable {}
