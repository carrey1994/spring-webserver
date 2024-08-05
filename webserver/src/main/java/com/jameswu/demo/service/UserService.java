package com.jameswu.demo.service;

import com.jameswu.demo.model.entity.Coupon;
import com.jameswu.demo.model.entity.GcUser;
import com.jameswu.demo.model.entity.UserProfile;
import com.jameswu.demo.model.payload.UserProfilePayload;
import com.jameswu.demo.repository.CouponRepository;
import com.jameswu.demo.repository.UserProfileRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

	private final UserProfileRepository userProfileRepository;
	private final CouponRepository couponRepository;

	@Autowired
	public UserService(UserProfileRepository userProfileRepository, CouponRepository couponRepository) {
		this.userProfileRepository = userProfileRepository;
		this.couponRepository = couponRepository;
	}

	@Transactional
	public UserProfile updateUserProfile(GcUser user, UserProfilePayload userProfilePayload) {
		user.getProfile().setAddress(userProfilePayload.address());
		user.getProfile().setEmail(userProfilePayload.email());
		return userProfileRepository.save(user.getProfile());
	}

	public List<Coupon> getCouponsByUser(GcUser user, Pageable pageable) {
		return couponRepository.findAllByUserUserId(user.getUserId(), pageable);
	}
}
