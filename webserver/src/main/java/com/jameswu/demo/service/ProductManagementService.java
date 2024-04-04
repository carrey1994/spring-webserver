package com.jameswu.demo.service;

import com.jameswu.demo.model.ProductPayload;
import com.jameswu.demo.model.Specials;
import com.jameswu.demo.model.entity.Product;
import com.jameswu.demo.repository.ProductRepository;
import com.jameswu.demo.utils.RedisKey;
import org.redisson.api.RMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductManagementService {

    private final ProductRepository productRepository;
    private final RedisService redisService;

    @Autowired
    public ProductManagementService(ProductRepository productRepository, RedisService redisService) {
        this.productRepository = productRepository;
        this.redisService = redisService;
    }

    @Transactional
    public Product add(ProductPayload payload) {
        return productRepository.save(
                new Product(payload.title(), payload.description(), payload.price(), payload.quantity()));
    }

    public RMap<String, String> addSpecials(Specials payload) {
        return redisService.setHashMap(RedisKey.withProductPrefix(payload.productId()), payload.specialsDetail());
    }
}
