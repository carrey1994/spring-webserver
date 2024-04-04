package com.jameswu.demo.repository;

import com.jameswu.demo.model.entity.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<Order, Integer> {}
