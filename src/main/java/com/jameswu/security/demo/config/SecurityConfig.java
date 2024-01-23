package com.jameswu.security.demo.config;

import com.jameswu.security.demo.model.UserRole;
import com.jameswu.security.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(CsrfConfigurer::disable)
                .httpBasic(withDefaults())
                .authorizeHttpRequests(
                        authz -> authz
                                .requestMatchers("/health-checker/**")
                                .permitAll()
                                .requestMatchers("/index/hello")
                                .permitAll()
                                .requestMatchers("/user/management/all")
                                .hasRole(UserRole.ADMIN.name())
                                .requestMatchers("/logout/**")
                                .permitAll()
                                .requestMatchers("/")
                                .permitAll()
                                .anyRequest().authenticated());
        return http.build();
    }
}
