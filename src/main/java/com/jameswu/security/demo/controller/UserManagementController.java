package com.jameswu.security.demo.controller;

import com.jameswu.security.demo.model.UserPayload;
import com.jameswu.security.demo.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "/user/management")
public class UserManagementController {
  @Autowired private UserManagementService userManagementService;

  @PostMapping("add")
  public void addUser(UserPayload userPayload) {
    userManagementService.addUser(userPayload);
  }
}
