package com.jameswu.demo.controller;

import com.jameswu.demo.model.UserPayload;
import com.jameswu.demo.model.entity.UserProfile;
import com.jameswu.demo.model.response.Result;
import com.jameswu.demo.model.response.SuccessResult;
import com.jameswu.demo.service.RedisService;
import com.jameswu.demo.service.UserManagementService;
import com.jameswu.demo.utils.RedisKey;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/management")
@Validated
public class UserManagementController {
    private final UserManagementService userManagementService;
    private final RedisService redisService;
    private final Logger logger = LoggerFactory.getLogger(UserManagementController.class);

    @Autowired
    public UserManagementController(UserManagementService userManagementService, RedisService redisService) {
        this.userManagementService = userManagementService;
        this.redisService = redisService;
    }

    @PostMapping("register")
    @Transactional
    public Result<UserProfile> register(@RequestBody @Valid UserPayload userPayload) {
        try {
            redisService.tryPartialLock(RedisKey.PREFIX_ADD_USER, userPayload.username());
            UserProfile newUserProfile = userManagementService.register(userPayload);
            return new SuccessResult<>(newUserProfile);
        } finally {
            redisService.tryPartialUnlock(RedisKey.PREFIX_ADD_USER, userPayload.username());
        }
    }

    @GetMapping("all")
    public Result<List<UserProfile>> activeUsers(Pageable pageable) {
        return new SuccessResult<>(userManagementService.activeUsers(pageable).getContent());
    }
}
