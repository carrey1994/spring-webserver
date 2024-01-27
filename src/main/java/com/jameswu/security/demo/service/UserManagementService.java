package com.jameswu.security.demo.service;

import com.jameswu.security.demo.exception.UserNotFoundException;
import com.jameswu.security.demo.model.GcUser;
import com.jameswu.security.demo.model.UserPayload;
import com.jameswu.security.demo.model.UserProfile;
import com.jameswu.security.demo.model.UserRole;
import com.jameswu.security.demo.model.UserStatus;
import com.jameswu.security.demo.repository.CompanyRepository;
import com.jameswu.security.demo.repository.UserRepository;
import com.jameswu.security.demo.utils.GzTexts;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserManagementService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private static final Logger logger = Logger.getLogger(UserManagementService.class.getName());

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
