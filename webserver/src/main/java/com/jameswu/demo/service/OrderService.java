package com.jameswu.demo.service;

import com.jameswu.demo.model.NewOrderPayload;
import com.jameswu.demo.model.entity.GcUser;
import com.jameswu.demo.model.entity.Order;
import com.jameswu.demo.model.entity.Product;
import com.jameswu.demo.repository.OrderRepository;
import com.jameswu.demo.repository.ProductRepository;
import com.jameswu.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public Order createOrder(NewOrderPayload payload) {
        GcUser gcUser = userRepository
                .findByUserId(payload.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Product product = productRepository
                .findById(payload.productId())
                .orElseThrow(() -> new IllegalArgumentException("product not found"));
        //        return productOrderRepository.save(new Order(gcUser, product));
        return null;
    }
}
