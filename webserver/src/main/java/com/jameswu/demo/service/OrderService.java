package com.jameswu.demo.service;

import com.jameswu.demo.model.entity.GcUser;
import com.jameswu.demo.model.entity.Order;
import com.jameswu.demo.model.entity.OrderDetail;
import com.jameswu.demo.model.entity.Product;
import com.jameswu.demo.model.payload.BuyingProductPayload;
import com.jameswu.demo.model.payload.NewOrderPayload;
import com.jameswu.demo.model.payload.SpecialsDetailPayload;
import com.jameswu.demo.model.payload.SpecialsPayload;
import com.jameswu.demo.repository.OrderDetailRepository;
import com.jameswu.demo.repository.OrderRepository;
import com.jameswu.demo.repository.ProductRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.jameswu.demo.utils.RedisKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final RedisService redisService;

    @Autowired
    public OrderService(
            OrderRepository orderRepository,
            ProductRepository productRepository,
            OrderDetailRepository orderDetailRepository,
            RedisService redisService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.redisService = redisService;
    }

    @Transactional
    public Order createOrder(GcUser gcUser, NewOrderPayload payload) {
        Order order = orderRepository.save(new Order(gcUser, Set.of()));
        Iterable<Product> restoredProducts = productRepository.findAllById(
                payload.keySet().stream().map(Integer::valueOf).toList());
        Set<OrderDetail> detailList = new HashSet<>();
        restoredProducts.forEach(restoredProduct -> {
            BuyingProductPayload buyingProduct =
                    payload.get(String.valueOf(restoredProduct.getProductId()));
            int buyingQuantity = buyingProduct.quantity();
            int updatedQuantity = restoredProduct.getQuantity() - buyingQuantity;
            if (updatedQuantity < 0) {
                throw new IllegalArgumentException("Product quantity not enough");
            }
            restoredProduct.setQuantity(updatedQuantity);
            OrderDetail orderDetail = new OrderDetail(
                    restoredProduct,
                    order,
                    buyingQuantity,
                    restoredProduct.getPrice(),
                    buyingProduct.couponId());
            detailList.add(orderDetail);
        });
        order.setOrderDetails(detailList);
        orderDetailRepository.saveAll(detailList);
        return order;
    }

    public List<Product> createSpecialsOrder(NewOrderPayload newOrderPayload) {
        // todo:
        // 1. reduce the quantity in redis, make sure this step is atomic. lua or maybe reddison
        // support
        // 2. mock payment and create order to redis queue
        // 3. async order saving to database
        List<Integer> productIds = newOrderPayload.keySet().stream().map(Integer::parseInt).toList();
        redisService.executeEvalSha();
        productIds.forEach(id -> {
                    SpecialsDetailPayload hashClass = redisService.getHashClass(RedisKey.withSpecialsPrefix(id), SpecialsDetailPayload.class);
                }
        );
        return List.of();
    }
}
