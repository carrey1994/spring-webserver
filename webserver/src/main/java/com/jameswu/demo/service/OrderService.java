package com.jameswu.demo.service;

import com.jameswu.demo.model.NewOrderPayload;
import com.jameswu.demo.model.entity.GcUser;
import com.jameswu.demo.model.entity.Insurance;
import com.jameswu.demo.model.entity.InsuranceOrder;
import com.jameswu.demo.repository.InsuranceOrderRepository;
import com.jameswu.demo.repository.InsuranceRepository;
import com.jameswu.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private InsuranceOrderRepository insuranceOrderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InsuranceRepository insuranceRepository;

    public InsuranceOrder createOrder(NewOrderPayload payload) {
        GcUser gcUser = userRepository
                .findByUserId(payload.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Insurance insurance = insuranceRepository
                .findById(payload.insuranceId())
                .orElseThrow(() -> new IllegalArgumentException("Insurance not found"));
        return insuranceOrderRepository.save(new InsuranceOrder(gcUser, insurance));
    }
}
