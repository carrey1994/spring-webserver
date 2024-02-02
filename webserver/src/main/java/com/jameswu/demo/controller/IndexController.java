package com.jameswu.demo.controller;

import com.jameswu.demo.model.LoginPayload;
import com.jameswu.demo.model.entity.GcUser;
import com.jameswu.demo.model.response.Result;
import com.jameswu.demo.model.response.SuccessResult;
import com.jameswu.demo.service.JwtService;
import com.jameswu.demo.utils.GzTexts;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class IndexController {

    @Autowired
    public IndexController(JwtService jwtService, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("login")
    public Result<Map<String, String>> login(@RequestBody LoginPayload payload) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(payload.username(), payload.password()));
        String jwt = jwtService.generateToken((GcUser) authentication.getPrincipal());
        return new SuccessResult<>(Map.of(GzTexts.ACCESS_TOKEN, jwt));
    }
}
