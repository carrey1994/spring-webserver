package com.jameswu.demo.controller;

import com.jameswu.demo.model.entity.GcUser;
import com.jameswu.demo.model.entity.Order;
import com.jameswu.demo.model.entity.Product;
import com.jameswu.demo.model.payload.NewOrderPayload;
import com.jameswu.demo.model.response.Result;
import com.jameswu.demo.service.OrderService;
import com.jameswu.demo.service.RedisService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order")
@Validated
public class OrderController {

	@Autowired private OrderService orderService;

	@Autowired private RedisService redisService;

	@PostMapping("create")
	public Result<Order> createOrder(
			Authentication authentication, @RequestBody @Valid NewOrderPayload newOrderPayload) {
		return Result.success(
				orderService.createOrder((GcUser) authentication.getPrincipal(), newOrderPayload));
	}

	@GetMapping("specials/create")
	public Result<List<Product>> createSpecialsOrder(
			@RequestBody @Valid NewOrderPayload newOrderPayload) {
		return Result.success(orderService.createSpecialsOrder(newOrderPayload));
	}
}
