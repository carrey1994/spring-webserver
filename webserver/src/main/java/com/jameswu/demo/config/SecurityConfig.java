package com.jameswu.demo.config;

import com.jameswu.demo.filter.JwtAuthenticationFilter;
import com.jameswu.demo.model.enums.UserRole;
import com.jameswu.demo.service.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final JwtService jwtService;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		String[] publicRouter = new String[] {
			"/",
			"/api/v1/public/**",
			"/api/v1/api-docs/**",
			"/actuator/**",
			"/*.svg",
			"/index.html",
			"/swagger-ui/**",
			"/main.css",
			"/app.js",
			"/favicon.ico",
			"/gs-guide-websocket/**"
		};

		http.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(authz -> authz.requestMatchers(publicRouter)
						.permitAll()
						.requestMatchers("/api/v1/user/management/**")
						.hasAnyRole(UserRole.ADMIN.name())
						.requestMatchers("/api/v1/product/management/**")
						.hasRole(UserRole.ADMIN.name())
						.anyRequest()
						.authenticated())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.logout(logout -> logout.logoutUrl("/api/v1/logout")
						.logoutSuccessHandler((request, response, authentication) -> {
							jwtService.removeToken(request);
							SecurityContextHolder.clearContext();
						}));
		return http.build();
	}
}
