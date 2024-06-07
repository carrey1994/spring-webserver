package com.jameswu.demo.service;

import com.jameswu.demo.model.entity.GcUser;
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
	public UserProfile updateUserProfile(GcUser user, UserProfilePayload userProfilePayload) {
		user.getProfile().setAddress(userProfilePayload.address());
		user.getProfile().setEmail(userProfilePayload.email());
		return userProfileRepository.save(user.getProfile());
	}
}
