package com.jameswu.security.demo.service;

import com.jameswu.security.demo.exception.UserNotFoundException;
import com.jameswu.security.demo.model.GcUser;
import com.jameswu.security.demo.model.UserPayload;
import com.jameswu.security.demo.model.UserProfile;
import com.jameswu.security.demo.model.UserRole;
import com.jameswu.security.demo.model.UserStatus;
import com.jameswu.security.demo.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserManagementService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public UserProfile addUser(UserPayload userPayload) {
        userRepository.findByUsername(userPayload.getUsername()).ifPresent(gcUser -> {
            throw new UserNotFoundException("User already exists.");
        });
        if (userPayload.getRecommenderId() != null) {
            userRepository.findByUserId(userPayload.getRecommenderId()).ifPresentOrElse(e -> {}, () -> {
                throw new UserNotFoundException("The recommender not found.");
            });
        }
        UUID newUserId = UUID.randomUUID();
        UserProfile userProfile = new UserProfile(
                newUserId,
                userPayload.getEmail(),
                userPayload.getAddress(),
                userPayload.getDate(),
                userPayload.getRecommenderId());
        GcUser newUser = GcUser.builder()
                .userId(newUserId)
                .username(userPayload.getUsername())
                .password(bCryptPasswordEncoder.encode(userPayload.getPassword()))
                .userRole(UserRole.USER)
                .userStatus(UserStatus.ACTIVE)
                .userProfile(userProfile)
                .build();
        userRepository.save(newUser);
        return userProfile;
    }

    public List<UserProfile> allUsers() {
        return userRepository.findAll().stream().map(GcUser::getUserProfile).collect(Collectors.toList());
    }
}
