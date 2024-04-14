package com.jameswu.demo.repository;

import com.jameswu.demo.model.entity.Coupon;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends CrudRepository<Coupon, UUID> {}
