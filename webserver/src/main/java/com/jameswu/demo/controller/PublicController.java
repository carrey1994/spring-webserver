package com.jameswu.demo.controller;

import com.jameswu.demo.model.UserPayload;
import com.jameswu.demo.model.entity.UserProfile;
import com.jameswu.demo.model.response.Result;
import com.jameswu.demo.model.response.SuccessResult;
import com.jameswu.demo.service.HealthService;
import com.jameswu.demo.service.UserManagementService;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.GitProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public")
public class PublicController {

    @Autowired
    public PublicController(
            HealthService healthService, GitProperties gitProperties, UserManagementService userManagementService1) {
        this.healthService = healthService;
        this.gitProperties = gitProperties;
        this.userManagementService = userManagementService1;
    }

    private final HealthService healthService;
    private final GitProperties gitProperties;
    private final UserManagementService userManagementService;

    @PostMapping("register")
    public Result<UserProfile> register(@RequestBody @Valid UserPayload userPayload) {
        UserProfile newUserProfile = userManagementService.register(userPayload);
        return new SuccessResult<>(newUserProfile);
    }

    @GetMapping("health")
    public Result<String> checkHealth() {
        return new SuccessResult<>(healthService.checkHealth());
    }

    @GetMapping("version")
    public Result<Map<String, String>> version() {
        throw new IllegalArgumentException("test");
        //        String commitId = gitProperties.getCommitId();
        //        String[] tags = Optional.ofNullable(Strings.split(gitProperties.get("tags"), ","))
        //                .orElse(new String[] {GzTexts.NONE});
        //        return new SuccessResult<>(Map.of("id", commitId, "tag", tags[tags.length - 1]));
    }
}
