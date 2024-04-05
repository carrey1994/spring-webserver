package com.jameswu.demo.controller;

import com.jameswu.demo.model.payload.LoginPayload;
import com.jameswu.demo.model.response.Result;
import com.jameswu.demo.service.IndexService;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class IndexController {

	@Autowired
	public IndexController(IndexService indexService) {
		this.indexService = indexService;
	}

	private final IndexService indexService;

	@PostMapping("login")
	public Result<Map<String, String>> login(@Valid @RequestBody LoginPayload payload) {
		return Result.success(indexService.login(payload));
	}
}
