package com.jameswu.demo.controller;

import com.jameswu.demo.model.NewOrderPayload;
import com.jameswu.demo.model.entity.InsuranceOrder;
import com.jameswu.demo.model.response.SuccessResult;
import com.jameswu.demo.service.OrderService;
import com.jameswu.demo.service.RedisService;
import com.jameswu.demo.utils.RedisKey;
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
public class InsuranceOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisService redisService;

    @PostMapping("create")
    @Transactional
    public SuccessResult<InsuranceOrder> createOrder(@RequestBody @Valid NewOrderPayload newOrderPayload) {
        try {
            redisService.tryPartialLock(RedisKey.PREFIX_CREATE_ORDER, String.valueOf(newOrderPayload.userId()));
            return new SuccessResult<>(orderService.createOrder(newOrderPayload));
        } finally {
            redisService.tryPartialUnlock(RedisKey.PREFIX_CREATE_ORDER, String.valueOf(newOrderPayload.userId()));
        }
    }
}
