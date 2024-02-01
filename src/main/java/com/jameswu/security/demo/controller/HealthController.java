package com.jameswu.security.demo.controller;

import com.jameswu.security.demo.model.Result;
import com.jameswu.security.demo.service.HealthService;
import com.jameswu.security.demo.service.UserManagementService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/health")
public class HealthController {

    @Autowired
    private static final Logger logger = Logger.getLogger(HealthController.class.getName());

    @Autowired
    private HealthService healthService;

    @Autowired
    private UserManagementService userManagementService;

    @GetMapping("check")
    public Result<String> checkHealth() {
        return new Result<>(healthService.checkHealth());
    }
}
