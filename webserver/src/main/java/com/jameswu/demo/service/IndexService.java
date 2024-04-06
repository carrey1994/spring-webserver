package com.jameswu.demo.service;

import com.jameswu.demo.model.entity.GcUser;
import com.jameswu.demo.model.payload.LoginPayload;
import com.jameswu.demo.utils.GzTexts;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class IndexService {

	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

	@Autowired
	public IndexService(JwtService jwtService, AuthenticationManager authenticationManager) {
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
	}

	public Map<String, String> login(LoginPayload payload) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(payload.username(), payload.password()));
		String jwt = jwtService.generateToken((GcUser) authentication.getPrincipal());
		return Map.of(GzTexts.ACCESS_TOKEN, jwt);
	}
}
