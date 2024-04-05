package com.jameswu.demo.service;

import com.jameswu.demo.model.entity.GcProfileLevel;
import com.jameswu.demo.model.entity.GcProfileTreeNode;
import com.jameswu.demo.model.entity.GcUser;
import com.jameswu.demo.model.entity.UserProfile;
import com.jameswu.demo.model.enums.UserRole;
import com.jameswu.demo.model.enums.UserStatus;
import com.jameswu.demo.model.payload.UserPayload;
import com.jameswu.demo.notification.NotificationService;
import com.jameswu.demo.notification.mail.QueueTag;
import com.jameswu.demo.repository.GcProfileLevelRepository;
import com.jameswu.demo.repository.UserRepository;
import com.jameswu.demo.utils.GzTexts;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
            NotificationService notificationService,
            GcProfileLevelRepository gcProfileLevelRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.notificationService = notificationService;
        this.gcProfileLevelRepository = gcProfileLevelRepository;
    }

    private final GcProfileLevelRepository gcProfileLevelRepository;
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

    public GcProfileTreeNode searchingDiagram(int userId) {
        return queryByUserIdAndLevel(userId, 4);
    }

    public GcProfileTreeNode searchingChildren(int userId) {
        return queryByUserIdAndLevel(userId, 2);
    }

    private GcProfileTreeNode queryByUserIdAndLevel(int userId, int level) {
        List<GcProfileLevel> gcProfileLevelList = gcProfileLevelRepository.queryChildren(userId, level);
        if (gcProfileLevelList.size() == 1) {
            // no any child, return user only.
            return new GcProfileTreeNode(gcProfileLevelList.get(0).toUserProfile(), new ArrayList<>());
        }
        return mappingChildren(gcProfileLevelList, userId);
    }

    private GcProfileTreeNode mappingChildren(List<GcProfileLevel> gcProfileLevelList, int userId) {
        GcProfileLevel rootProfileLevel = gcProfileLevelList.stream()
                .filter((profileLevel) -> profileLevel.getUserId() == userId)
                .toList()
                .get(0);
        Map<Integer, List<GcProfileLevel>> collect = gcProfileLevelList.stream()
                .filter(e -> e.getRecommenderId() != null)
                .collect(Collectors.groupingBy(GcProfileLevel::getRecommenderId));
        List<GcProfileTreeNode> children = collect.get(userId).stream()
                .map(e -> new GcProfileTreeNode(e.toUserProfile(), new ArrayList<>()))
                .toList();
        GcProfileTreeNode rootNode = new GcProfileTreeNode(rootProfileLevel.toUserProfile(), children);
        createTreeDiagram(collect, rootNode);
        return rootNode;
    }

    private void createTreeDiagram(Map<Integer, List<GcProfileLevel>> collect, GcProfileTreeNode node) {
        if (collect.get(node.getUserProfile().getUserId()) == null) {
            return;
        }
        List<GcProfileTreeNode> children = collect.get(node.getUserProfile().getUserId()).stream()
                .map(e -> new GcProfileTreeNode(e.toUserProfile(), new ArrayList<>()))
                .toList();
        node.setChildrenProfiles(children);
        for (GcProfileTreeNode child : children) {
            createTreeDiagram(collect, child);
        }
    }
}
