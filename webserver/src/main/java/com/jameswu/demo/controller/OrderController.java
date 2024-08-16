package com.jameswu.demo.controller;

import com.jameswu.demo.model.entity.GcUser;
import com.jameswu.demo.model.entity.Order;
import com.jameswu.demo.model.entity.Product;
import com.jameswu.demo.model.payload.OrderDetailPayload;
import com.jameswu.demo.model.response.Result;
import com.jameswu.demo.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order")
@Validated
@AllArgsConstructor
public class OrderController {

	private OrderService orderService;

	@PostMapping("create")
	public Result<Order> createOrder(
			Authentication authentication, @RequestBody @Valid @NotNull List<OrderDetailPayload> orderDetails) {
		return Result.success(orderService.createOrder((GcUser) authentication.getPrincipal(), orderDetails));
	}

	@PostMapping("specials/create")
	public Result<List<Product>> createSpecialsOrder(
			Authentication authentication, @RequestBody @Valid @NotNull List<OrderDetailPayload> orderDetails) {
		return Result.success(orderService.createSpecialsOrder((GcUser) authentication.getPrincipal(), orderDetails));
	}
}
