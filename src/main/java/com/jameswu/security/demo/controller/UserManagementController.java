package com.jameswu.security.demo.controller;

import com.jameswu.security.demo.model.Result;
import com.jameswu.security.demo.model.UserPayload;
import com.jameswu.security.demo.model.UserProfile;
import com.jameswu.security.demo.service.UserManagementService;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private UserManagementService userManagementService;

    private Logger logger = LoggerFactory.getLogger(UserManagementController.class);

    @PostMapping("add")
    public Result<UserProfile> addUser(@RequestBody @Valid UserPayload userPayload) {
        logger.info("--- ADD NEW USER ---");
        UserProfile newUserProfile = userManagementService.addUser(userPayload);
        return new Result<>(newUserProfile);
    }

    @GetMapping("all")
    @Transactional
    public Result<List<UserProfile>> allUserProfiles() {
        return new Result<>(userManagementService.allUsers());
    }
}
