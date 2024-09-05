package com.jameswu.demo.config;

import com.jameswu.demo.filter.JwtAuthenticationFilter;
import com.jameswu.demo.model.enums.UserRole;
import com.jameswu.demo.service.JwtService;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
				.cors(Customizer.withDefaults())
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

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000/"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS"));
		// Allow specific headers
		configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization", "X-Requested-With", "Accept"));

		// Allow credentials (cookies, authorization headers)
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
