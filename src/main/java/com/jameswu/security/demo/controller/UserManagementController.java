package com.jameswu.security.demo.controller;

import com.jameswu.security.demo.model.GcUser;
import com.jameswu.security.demo.model.UserPayload;
import com.jameswu.security.demo.model.UserProfile;
import com.jameswu.security.demo.service.UserManagementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/management")
@Validated
public class UserManagementController {
    @Autowired
    private UserManagementService userManagementService;

    @PostMapping("add")
    public void addUser(@RequestBody @Valid UserPayload userPayload) {
        userManagementService.addUser(userPayload);
    }

    @GetMapping("all")
    public List<UserProfile> allUserProfiles() {
        return userManagementService.allUsers();
    }
}
