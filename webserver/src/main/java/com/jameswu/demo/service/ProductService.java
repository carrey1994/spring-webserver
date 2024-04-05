package com.jameswu.demo.service;

import com.jameswu.demo.model.entity.Product;
import com.jameswu.demo.model.payload.SpecialsDetailPayload;
import com.jameswu.demo.model.payload.SpecialsPayload;
import com.jameswu.demo.repository.ProductRepository;
import com.jameswu.demo.utils.RedisKey;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final RedisService redisService;
    private final CacheService cacheService;

    @Autowired
    public ProductService(ProductRepository productRepository, RedisService redisService, CacheService cacheService) {
        this.productRepository = productRepository;
        this.redisService = redisService;
        this.cacheService = cacheService;
    }

    public List<Product> all(Pageable pageable) {
        return productRepository.findAll(pageable).getContent();
    }

    public List<SpecialsPayload> specials() {
        // todo: cache table
        return List.of();
    }

    public SpecialsPayload specialsById(int productId) {
        return new SpecialsPayload(
                productId,
                redisService.getHashClass(RedisKey.withProductPrefix(productId), SpecialsDetailPayload.class));
    }
}
