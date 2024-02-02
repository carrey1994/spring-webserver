package com.jameswu.demo.config;

import com.jameswu.demo.model.entity.GcUser;
import com.jameswu.demo.model.entity.UserProfile;
import com.jameswu.demo.model.enums.UserRole;
import com.jameswu.demo.model.enums.UserStatus;
import com.jameswu.demo.repository.UserRepository;
import com.jameswu.demo.service.RedisService;
import com.jameswu.demo.utils.RedisKey;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@ConfigurationProperties(prefix = "init")
@Profile("!prod")
@Data
public class UserInitiationConfig {

    @Autowired
    public UserInitiationConfig(
            UserRepository userRepository,
            RedisService redisService,
            BCryptPasswordEncoder passwordEncoder,
            List<InitUserData> users) {
        this.userRepository = userRepository;
        this.redisService = redisService;
        this.passwordEncoder = passwordEncoder;
        this.users = users;
    }

    private UserRepository userRepository;
    private RedisService redisService;
    private BCryptPasswordEncoder passwordEncoder;
    private List<InitUserData> users;

    record InitUserData(long id, String username, String password, Long recommenderId, UserRole role) {}

    @Bean
    public void initUsers() {
        try {
            redisService.trySystemLock(RedisKey.LOCK_INIT_USERS);
            List<GcUser> gcUsers = users.stream()
                    .map(user -> GcUser.builder()
                            .userId(user.id)
                            .userStatus(UserStatus.ACTIVE)
                            .userRole(user.role)
                            .username(user.username)
                            .password(passwordEncoder.encode(user.password))
                            .profile(new UserProfile(
                                    user.id, user.username + "@gc.mail", "Taipei", LocalDate.now(), user.recommenderId))
                            .build())
                    .toList();
            userRepository.saveAll(gcUsers);
        } finally {
            redisService.trySystemUnlock(RedisKey.LOCK_INIT_USERS);
        }
    }
}
