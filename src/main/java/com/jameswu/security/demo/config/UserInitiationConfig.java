package com.jameswu.security.demo.config;

import com.jameswu.security.demo.model.GcUser;
import com.jameswu.security.demo.model.UserProfile;
import com.jameswu.security.demo.model.UserRole;
import com.jameswu.security.demo.model.UserStatus;
import com.jameswu.security.demo.repository.CompanyRepository;
import com.jameswu.security.demo.repository.RelationRepository;
import com.jameswu.security.demo.repository.UserRepository;
import com.jameswu.security.demo.service.RedisService;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@ConfigurationProperties(prefix = "init")
@Data
public class UserInitiationConfig {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RelationRepository relationRepository;

    @Autowired
    private RedisService redisService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private CompanyRepository companyRepository;

    private List<InitUserData> users;

    @Data
    static class InitUserData {
        private UUID id;
        private String username;
        private String password;
        private UUID recommenderId;
        private UserRole role;
    }

    @Bean
    public void initUsers() {
        RLock lock = redisService.tryLock("init-user");
        List<GcUser> gcUsers = users.stream()
                .map(user -> GcUser.builder()
                        .userId(user.id)
                        .userStatus(UserStatus.ACTIVE)
                        .userRole(user.role)
                        .username(user.username)
                        .password(passwordEncoder.encode(user.password))
                        .amount(0)
                        .userProfile(new UserProfile(
                                user.id, user.username + "@gc.mail", "Taipei", LocalDate.now(), user.recommenderId))
                        .build())
                .toList();
        userRepository.saveAll(gcUsers);
        redisService.unlock(lock);
    }
}
