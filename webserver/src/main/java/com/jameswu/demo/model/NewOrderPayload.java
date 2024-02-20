package com.jameswu.demo.model;

import java.io.Serializable;

public record NewOrderPayload(long insuranceId, long userId) implements Serializable {}
