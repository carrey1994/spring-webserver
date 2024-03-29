package com.jameswu.demo.repository;

import com.jameswu.demo.model.entity.InsuranceOrder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsuranceOrderRepository extends CrudRepository<InsuranceOrder, Long> {}
