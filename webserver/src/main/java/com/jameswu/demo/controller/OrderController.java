package com.jameswu.demo.controller;

import com.jameswu.demo.model.NewOrderPayload;
import com.jameswu.demo.model.entity.Order;
import com.jameswu.demo.model.response.SuccessResult;
import com.jameswu.demo.service.OrderService;
import com.jameswu.demo.service.RedisService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order")
@Validated
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisService redisService;

    @PostMapping("create")
    @Transactional
    public SuccessResult<Order> createOrder(@RequestBody @Valid NewOrderPayload newOrderPayload) {
        return new SuccessResult<>(orderService.createOrder(newOrderPayload));
    }
}
