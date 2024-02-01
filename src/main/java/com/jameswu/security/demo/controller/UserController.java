package com.jameswu.security.demo.controller;

import com.jameswu.security.demo.model.GcProfileTreeNode;
import com.jameswu.security.demo.model.Result;
import com.jameswu.security.demo.service.UserService;
import java.util.UUID;
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
    private UserService userService;

    @GetMapping("diagram")
    public Result<GcProfileTreeNode> diagram(@RequestParam("id") UUID userId) {
        return new Result<>(userService.diagram(userId));
    }

    @GetMapping("direct")
    public Result<GcProfileTreeNode> direct(@RequestParam("id") UUID userId) {
        return new Result<>(userService.onlyOneGenerationChild(userId));
    }
}
