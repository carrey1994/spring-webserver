package com.jameswu.demo.controller;

import com.jameswu.demo.model.entity.UserProfile;
import com.jameswu.demo.model.payload.LoginPayload;
import com.jameswu.demo.model.payload.RegisterPayload;
import com.jameswu.demo.model.response.Result;
import com.jameswu.demo.service.HealthService;
import com.jameswu.demo.service.UserManagementService;
import com.jameswu.demo.utils.GzTexts;
import io.jsonwebtoken.lang.Strings;
import jakarta.validation.Valid;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.boot.info.GitProperties;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public")
@AllArgsConstructor
public class PublicController {

	private final HealthService healthService;
	private final GitProperties gitProperties;
	private final UserManagementService userManagementService;

	@PostMapping("register")
	public Result<UserProfile> register(@RequestBody @Valid RegisterPayload registerPayload) {
		return Result.success(userManagementService.register(registerPayload));
	}

	@PostMapping("activate")
	public Result<UserProfile> active(@Param("token") String token) {
		return Result.success(userManagementService.activeUser(token));
	}

	@PostMapping("login")
	public Result<Map<String, String>> login(@Valid @RequestBody LoginPayload payload) {
		return Result.success(userManagementService.login(payload));
	}

	@GetMapping("health")
	public Result<String> checkHealth() {
		return Result.success((healthService.checkHealth()));
	}

	@GetMapping("version")
	public Result<Map<String, String>> version() {
		String commitId = gitProperties.getCommitId();
		String[] tags = Optional.ofNullable(Strings.split(gitProperties.get("tags"), ","))
				.orElse(new String[] {GzTexts.NONE});
		return Result.success((Map.of("id", commitId, "tag", tags[tags.length - 1])));
	}

	@GetMapping("long-time")
	public Result<String> longTime() throws InterruptedException {
		Thread.sleep(60 * 1000 * 2);
		return Result.success("OK");
	}
}
