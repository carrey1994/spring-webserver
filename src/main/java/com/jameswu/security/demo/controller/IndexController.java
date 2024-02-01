package com.jameswu.security.demo.controller;

import static com.jameswu.security.demo.utils.GzTexts.ACCESS_TOKEN;

import com.jameswu.security.demo.model.GcUser;
import com.jameswu.security.demo.model.LoginPayload;
import com.jameswu.security.demo.model.Result;
import com.jameswu.security.demo.repository.UserRepository;
import com.jameswu.security.demo.service.JwtService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class IndexController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("login")
    public Result<Map<String, String>> login(@RequestBody LoginPayload payload) {
        Result<Map<String, String>> result = new Result<>();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(payload.getUsername(), payload.getPassword()));
        String jwt = jwtService.generateToken((GcUser) authentication.getPrincipal());
        Map<String, String> map = new HashMap<>();
        map.put(ACCESS_TOKEN, jwt);
        result.setMessage(map);
        return result;
    }
}
