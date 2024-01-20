package com.jameswu.security.demo.config;

import com.jameswu.security.demo.model.GcUser;
import com.jameswu.security.demo.model.UserRole;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConf {

  @Autowired private BCryptPasswordEncoder passwordEncoder;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
            authorizationManagerRequestMatcherRegistry ->
                authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.DELETE)
                    .hasRole("ADMIN")
                    .requestMatchers("/admin/**")
                    .hasAnyRole("ADMIN")
                    .requestMatchers("/user/**")
                    .hasAnyRole("USER", "ADMIN")
                    .requestMatchers("/login/**")
                    .permitAll()
                    .requestMatchers("/")
                    .hasAnyRole("USER", "ADMIN")
                    .anyRequest()
                    .authenticated())
        .httpBasic(Customizer.withDefaults());

    return http.build();
  }

  @Bean
  public UserDetailsService userDetails(BCryptPasswordEncoder bCryptPasswordEncoder) {
    InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
    manager.createUser(
        //                GcUser.builder()
        //                        .userId(UUID.randomUUID())
        //                        .username("user")
        //                        .password(bCryptPasswordEncoder.encode("user"))
        //                        .userRole(UserRole.USER)
        //                        .authorities(List.of(new
        // SimpleGrantedAuthority(UserRole.USER.name())))
        //                        .build()
        User.withUsername("user")
            .password(bCryptPasswordEncoder.encode("user"))
            .roles("USER")
            .build());
    manager.createUser(
        GcUser.builder()
            .username("admin")
            .password(bCryptPasswordEncoder.encode("admin"))
            .userRole(UserRole.ADMIN)
            .authorities(
                Arrays.asList(
                    new SimpleGrantedAuthority(UserRole.USER.name()),
                    new SimpleGrantedAuthority(UserRole.ADMIN.name())))
            .build());
    return manager;
  }
}
