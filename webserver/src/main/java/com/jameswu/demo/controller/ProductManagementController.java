package com.jameswu.demo.controller;

import com.jameswu.demo.model.ProductPayload;
import com.jameswu.demo.model.Specials;
import com.jameswu.demo.model.entity.Product;
import com.jameswu.demo.model.response.Result;
import com.jameswu.demo.service.ProductManagementService;
import org.redisson.api.RMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/product/management")
public class ProductManagementController {

    @Autowired
    private ProductManagementService productManagementService;

    @PostMapping("add")
    public Result<Product> add(@RequestBody ProductPayload payload) {
        return Result.success(productManagementService.add(payload));
    }

    @PostMapping("specials/add")
    public Result<RMap<String, String>> addSpecials(@RequestBody Specials payload) {
        return Result.success(productManagementService.addSpecials(payload));
    }
}
