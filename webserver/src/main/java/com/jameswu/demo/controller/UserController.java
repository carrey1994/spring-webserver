package com.jameswu.demo.controller;

import com.jameswu.demo.model.entity.GcUser;
import com.jameswu.demo.model.entity.UserProfile;
import com.jameswu.demo.model.payload.UserProfilePayload;
import com.jameswu.demo.model.response.Result;
import com.jameswu.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@Validated
public class UserController {

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	private final UserService userService;

	@PutMapping("update")
	public Result<UserProfile> updateUserProfile(
			Authentication authentication, @Valid @RequestBody UserProfilePayload userProfile) {
		return Result.success(
				userService.updateUserProfile(
						((GcUser) authentication.getPrincipal()).getUserId(), userProfile));
	}

	@GetMapping("me")
	public Result<UserProfile> userProfile(Authentication authentication) {
		return Result.success(((GcUser) authentication.getPrincipal()).getProfile());
	}
}
