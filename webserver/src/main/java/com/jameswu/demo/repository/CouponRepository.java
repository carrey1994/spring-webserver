package com.jameswu.demo.repository;

import com.jameswu.demo.model.entity.Coupon;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends CrudRepository<Coupon, Integer> {
	List<Coupon> findAllByUserUserId(int userId, Pageable pageable);

	Optional<Coupon> findByCouponCode(UUID couponCode);
}
