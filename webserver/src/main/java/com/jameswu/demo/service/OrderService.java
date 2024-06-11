package com.jameswu.demo.service;

import com.jameswu.demo.model.entity.GcUser;
import com.jameswu.demo.model.entity.Order;
import com.jameswu.demo.model.entity.OrderDetail;
import com.jameswu.demo.model.entity.Product;
import com.jameswu.demo.model.payload.OrderDetailPayload;
import com.jameswu.demo.repository.OrderDetailRepository;
import com.jameswu.demo.repository.OrderRepository;
import com.jameswu.demo.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	private final OrderDetailRepository orderDetailRepository;
	private final RedisService redisService;
	private final RabbitService rabbitService;

	@Autowired
	public OrderService(
			OrderRepository orderRepository,
			ProductRepository productRepository,
			OrderDetailRepository orderDetailRepository,
			RedisService redisService,
			RabbitService rabbitService) {
		this.orderRepository = orderRepository;
		this.productRepository = productRepository;
		this.orderDetailRepository = orderDetailRepository;
		this.redisService = redisService;
		this.rabbitService = rabbitService;
	}

	@Transactional
	public Order createOrder(GcUser gcUser, List<OrderDetailPayload> orderDetails) {
		Order order = new Order(gcUser, Set.of());
		Iterable<Product> restoredProducts = productRepository.findAllById(
				orderDetails.stream().map(OrderDetailPayload::productId).toList());
		Map<Integer, OrderDetailPayload> productMap =
				orderDetails.stream().collect(Collectors.toMap(OrderDetailPayload::productId, Function.identity()));
		Set<OrderDetail> detailList = new HashSet<>();
		restoredProducts.forEach(restoredProduct -> {
			OrderDetailPayload buyingProduct = productMap.get(restoredProduct.getProductId());
			int buyingQuantity = buyingProduct.quantity();
			int updatedQuantity = restoredProduct.getQuantity() - buyingQuantity;
			if (updatedQuantity < 0) {
				throw new IllegalArgumentException("Product booked not enough");
			}
			restoredProduct.setQuantity(updatedQuantity);
			OrderDetail orderDetail = new OrderDetail(
					restoredProduct,
					order,
					buyingQuantity,
					restoredProduct.getPrice(),
					buyingProduct.couponId(),
					BigDecimal.ZERO);
			detailList.add(orderDetail);
		});
		order.setOrderDetails(detailList);
		orderRepository.save(order);
		return order;
	}

	public List<Product> createSpecialsOrder(GcUser user, List<OrderDetailPayload> orderDetails) {
		// todo:
		// 1. reduce the booked in redis, make sure this step is atomic. lua or maybe reddison
		// support
		// 2. mock payment and create order to redis queue
		// 3. async order saving to database
		//        List<Integer> productIds =
		//                newOrderPayload.keySet().stream().map(Integer::parseInt).toList();
		//		redisService.executeEvalSha();

		//		rabbitService.sendEmail(new SpecialOrderPayload(user.getUserId(), newOrderPayload.));
		//        productIds.forEach(id -> {
		//			new SpecialOrderPayload(user.getUserId(), newOrderPayload.get(id).)

		//			SpecialsDetailPayload hashClass = redisService.getHashClass(
		//					RedisKey.withSpecialsPrefix(id), SpecialsDetailPayload.class);
		//        });
		return List.of();
	}
}
