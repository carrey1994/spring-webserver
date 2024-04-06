package com.jameswu.demo.service;

import com.jameswu.demo.model.entity.UserProfile;
import com.jameswu.demo.model.payload.UserProfilePayload;
import com.jameswu.demo.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

	private final UserProfileRepository userProfileRepository;

	@Autowired
	public UserService(UserProfileRepository userProfileRepository) {
		this.userProfileRepository = userProfileRepository;
	}

	@Transactional
	public UserProfile updateUserProfile(int userId, UserProfilePayload userProfilePayload) {
		UserProfile userProfile = userProfileRepository
				.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("User not found"));
		userProfile.setAddress(userProfilePayload.address());
		userProfile.setEmail(userProfilePayload.email());
		return userProfileRepository.save(userProfile);
	}
}
