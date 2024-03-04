package com.jameswu.demo.controller;

import com.jameswu.demo.model.response.Result;
import com.jameswu.demo.model.response.SuccessResult;
import com.jameswu.demo.repository.UserRepository;
import com.jameswu.demo.service.HealthService;
import com.jameswu.demo.utils.GzTexts;
import io.jsonwebtoken.lang.Strings;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.GitProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public")
public class PublicController {

    @Autowired
    public PublicController(HealthService healthService, GitProperties gitProperties, UserRepository userRepository) {
        this.healthService = healthService;
        this.gitProperties = gitProperties;
    }

    private final HealthService healthService;
    private final GitProperties gitProperties;

    @GetMapping("health")
    public Result<String> checkHealth() {
        return new SuccessResult<>(healthService.checkHealth());
    }

    @GetMapping("version")
    public Result<Map<String, String>> version() {
        String commitId = gitProperties.getCommitId();
        String[] tags = Optional.ofNullable(Strings.split(gitProperties.get("tags"), ","))
                .orElse(new String[] {GzTexts.NONE});
        return new SuccessResult<>(Map.of("id", commitId, "tag", tags[tags.length - 1]));
    }
}
