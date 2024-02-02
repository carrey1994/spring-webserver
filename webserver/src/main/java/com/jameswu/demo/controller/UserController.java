package com.jameswu.demo.controller;

import com.jameswu.demo.model.entity.GcProfileTreeNode;
import com.jameswu.demo.model.response.Result;
import com.jameswu.demo.model.response.SuccessResult;
import com.jameswu.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@Validated
public class UserController {

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    private final UserService userService;

    @GetMapping("diagram")
    public Result<GcProfileTreeNode> diagram(@RequestParam("id") Long userId) {
        return new SuccessResult<>(userService.searchingDiagram(userId));
    }

    @GetMapping("direct")
    public Result<GcProfileTreeNode> direct(@RequestParam("id") Long userId) {
        return new SuccessResult<>(userService.searchingChildren(userId));
    }
}
