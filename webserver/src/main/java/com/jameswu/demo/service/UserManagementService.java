package com.jameswu.demo.service;

import com.jameswu.demo.model.UserPayload;
import com.jameswu.demo.model.entity.GcUser;
import com.jameswu.demo.model.entity.UserProfile;
import com.jameswu.demo.model.enums.UserRole;
import com.jameswu.demo.model.enums.UserStatus;
import com.jameswu.demo.notification.NotificationService;
import com.jameswu.demo.notification.mail.QueueTag;
import com.jameswu.demo.repository.UserRepository;
import com.jameswu.demo.utils.GzTexts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserManagementService {

    @Autowired
    public UserManagementService(
            UserRepository userRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            NotificationService notificationService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.notificationService = notificationService;
    }

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final NotificationService notificationService;

    @Transactional
    public UserProfile register(UserPayload userPayload) {
        userRepository.findByUsername(userPayload.username()).ifPresent(gcUser -> {
            throw new IllegalArgumentException(GzTexts.USER_ALREADY_EXISTS);
        });
        if (userPayload.recommenderId() != null) {
            userRepository.findById(userPayload.recommenderId()).ifPresentOrElse(action -> {}, () -> {
                throw new IllegalArgumentException(GzTexts.USER_NOT_FOUND);
            });
        }

        UserProfile userProfile = UserProfile.builder()
                .email(userPayload.email())
                .address(userPayload.address())
                .enrollmentDate(userPayload.date())
                .recommenderId(userPayload.recommenderId())
                .build();

        GcUser newUser = GcUser.builder()
                .username(userPayload.username())
                .password(bCryptPasswordEncoder.encode(userPayload.password()))
                .userRole(UserRole.USER)
                .userStatus(UserStatus.ACTIVE)
                .profile(userProfile)
                .build();

        userRepository.save(newUser);
        notificationService.putQueue(QueueTag.NEW_USER_TAG, userProfile);
        return newUser.getProfile();
    }

    public Page<UserProfile> activeUsers(Pageable pageable) {
        /* Don't use user repo calling findAll to get user profiles, it makes n+1 queries. */
        return userRepository.findByUserStatus(UserStatus.ACTIVE, pageable).map(GcUser::getProfile);
    }

    public Boolean systemBroadCast() {
        userRepository.findAll().forEach(gcUser -> notificationService.putQueue(QueueTag.SYSTEM, gcUser.getProfile()));
        return true;
    }
}
