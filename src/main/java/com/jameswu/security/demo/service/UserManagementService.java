package com.jameswu.security.demo.service;

import com.jameswu.security.demo.exception.UserNotFoundException;
import com.jameswu.security.demo.model.GcUser;
import com.jameswu.security.demo.model.UserPayload;
import com.jameswu.security.demo.model.UserProfile;
import com.jameswu.security.demo.model.UserRole;
import com.jameswu.security.demo.model.UserStatus;
import com.jameswu.security.demo.repository.UserRepository;
import com.jameswu.security.demo.utils.GzTexts;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserManagementService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public int test() {
        GcUser user = userRepository.findAll().get(0);
        int updatedAmount = user.getAmount() - 100;
        user.setAmount(updatedAmount);
        return userRepository.save(user).getAmount();
    }

    public UserProfile addUser(UserPayload userPayload) {
        UUID newUserId = UUID.randomUUID();
        userRepository.findByUsername(userPayload.getUsername()).ifPresent(gcUser -> {
            throw new UserNotFoundException(GzTexts.USER_ALREADY_EXISTS);
        });
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
