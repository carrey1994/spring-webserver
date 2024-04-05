package com.jameswu.demo.controller;

import com.jameswu.demo.model.entity.Product;
import com.jameswu.demo.model.payload.ProductPayload;
import com.jameswu.demo.model.payload.SpecialsPayload;
import com.jameswu.demo.model.response.Result;
import com.jameswu.demo.service.ProductManagementService;
import jakarta.validation.Valid;
import org.redisson.api.RMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/product/management")
@Validated
public class ProductManagementController {

    @Autowired
    private ProductManagementService productManagementService;

    @PostMapping("add")
    public Result<Product> add(@Valid @RequestBody ProductPayload payload) {
        return Result.success(productManagementService.add(payload));
    }

    @PostMapping("specials/add")
    public Result<RMap<String, String>> addSpecials(@Valid @RequestBody SpecialsPayload payload) {
        return Result.success(productManagementService.addSpecials(payload));
    }
}
