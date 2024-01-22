package com.jameswu.security.demo.config;

import com.jameswu.security.demo.model.UserRole;
import com.jameswu.security.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http // .csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(
                        authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                                .requestMatchers("/health-checker")
                                .permitAll()
                                .requestMatchers("/index/hello")
                                .permitAll()
                                .requestMatchers("/user/management/all")
                                .hasRole(UserRole.ADMIN.name())
                                .requestMatchers("/logout/**")
                                .permitAll()
                                .requestMatchers("/")
                                .permitAll()
                                .anyRequest()
                                .authenticated())
                .formLogin(form -> form.loginPage("/login").permitAll());
        return http.build();
    }
}