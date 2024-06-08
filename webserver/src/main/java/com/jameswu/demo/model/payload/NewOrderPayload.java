package com.jameswu.demo.model.payload;

import java.io.Serializable;
import java.util.List;

public record NewOrderPayload(List<OrderDetailPayload> orderDetails) implements Serializable {}
