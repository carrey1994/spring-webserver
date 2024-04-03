package com.jameswu.demo.controller;

import com.jameswu.demo.model.entity.UserProfile;
import com.jameswu.demo.model.response.Result;
import com.jameswu.demo.model.response.SuccessResult;
import com.jameswu.demo.service.UserManagementService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/management")
@Validated
public class UserManagementController {
    private final UserManagementService userManagementService;
    private final Logger logger = LoggerFactory.getLogger(UserManagementController.class);

    @Autowired
    public UserManagementController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    @GetMapping("all")
    public Result<List<UserProfile>> activeUsers(Pageable pageable) {
        return new SuccessResult<>(userManagementService.activeUsers(pageable).getContent());
    }

    @GetMapping("system")
    public Result<Boolean> systemBroadcast() {
        return new SuccessResult<>(userManagementService.systemBroadCast());
    }
}
