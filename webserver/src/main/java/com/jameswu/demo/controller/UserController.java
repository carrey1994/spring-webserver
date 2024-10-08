package com.jameswu.demo.controller;

import com.jameswu.demo.model.entity.Coupon;
import com.jameswu.demo.model.entity.GcUser;
import com.jameswu.demo.model.entity.UserProfile;
import com.jameswu.demo.model.payload.UserProfilePayload;
import com.jameswu.demo.model.response.Result;
import com.jameswu.demo.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
public class UserController {

	private final UserService userService;

	@PutMapping("update")
	public Result<UserProfile> updateUserProfile(
			Authentication authentication, @Valid @RequestBody UserProfilePayload userProfile) {
		return Result.success(userService.updateUserProfile(((GcUser) authentication.getPrincipal()), userProfile));
	}

	@GetMapping("coupons")
	public Result<List<Coupon>> getCouponsByUser(Authentication authentication, Pageable pageable) {
		return Result.success(userService.getCouponsByUser(((GcUser) authentication.getPrincipal()), pageable));
	}

	// todo: frontend can get profile from jwt as well
	@GetMapping("me")
	public Result<UserProfile> userProfile(Authentication authentication) {
		return Result.success(((GcUser) authentication.getPrincipal()).getProfile());
	}
}
