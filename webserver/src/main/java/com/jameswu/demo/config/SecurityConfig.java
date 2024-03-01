package com.jameswu.demo.config;

import com.jameswu.demo.filter.JwtAuthenticationFilter;
import com.jameswu.demo.model.entity.UserProfile;
import com.jameswu.demo.model.enums.UserRole;
import com.jameswu.demo.service.CacheService;
import com.jameswu.demo.service.JwtService;
import com.jameswu.demo.utils.GzTexts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtService jwtService;
    private final CacheService cacheService;

    @Autowired
    public SecurityConfig(
            AuthenticationProvider authenticationProvider,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            JwtService jwtService,
            CacheService cacheService) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtService = jwtService;
        this.cacheService = cacheService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        String[] publicRouter = new String[] {
            "/",
            "/api/v1/login",
            "/api/v1/logout",
            "/api/v1/health/**",
            "/api/v1/api-docs/**",
            "/api/v1/public/**",
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
                        .hasRole(UserRole.ADMIN.name())
                        .anyRequest()
                        .authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout.logoutUrl("/api/v1/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            String jwt =
                                    request.getHeader(HttpHeaders.AUTHORIZATION).replace(GzTexts.BEARER_PREFIX, "");
                            jwtService.removeToken(jwt);
                            SecurityContextHolder.clearContext();
                            cacheService.removeIdFromUserCache(jwtService
                                    .parsePayload(jwt, JwtService.JWT_USER, UserProfile.class)
                                    .getUserId());
                        }));
        return http.build();
    }
}
