package com.jameswu.demo.config;

import com.jameswu.demo.model.entity.GcUser;
import com.jameswu.demo.model.entity.Insurance;
import com.jameswu.demo.model.entity.InsuranceOrder;
import com.jameswu.demo.model.entity.UserProfile;
import com.jameswu.demo.model.enums.UserRole;
import com.jameswu.demo.model.enums.UserStatus;
import com.jameswu.demo.repository.InsuranceOrderRepository;
import com.jameswu.demo.repository.InsuranceRepository;
import com.jameswu.demo.repository.UserRepository;
import com.jameswu.demo.service.RedisService;
import java.time.Instant;
import java.util.List;
import java.util.Random;
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
            InsuranceRepository insuranceRepository,
            RedisService redisService,
            BCryptPasswordEncoder passwordEncoder,
            List<InitUserData> users,
            InsuranceOrderRepository insuranceOrderRepository) {
        this.userRepository = userRepository;
        this.insuranceRepository = insuranceRepository;
        this.insuranceOrderRepository = insuranceOrderRepository;
        this.redisService = redisService;
        this.passwordEncoder = passwordEncoder;
        this.users = users;
    }

    private UserRepository userRepository;
    private InsuranceRepository insuranceRepository;
    private RedisService redisService;
    private BCryptPasswordEncoder passwordEncoder;
    private List<InitUserData> users;
    private InsuranceOrderRepository insuranceOrderRepository;

    record InitUserData(long id, String username, String password, Long recommenderId, UserRole role) {}

    @Bean
    public void initUsers() {
        Insurance a = new Insurance("A Ins.", "AA");
        Insurance b = new Insurance("B Ins.", "BB");
        Insurance c = new Insurance("C Ins.", "CC");
        var insurances = List.of(a, b, c);
        insuranceRepository.saveAll(List.of(a, b, c));
        List<GcUser> gcUsers = users.stream()
                .map(user -> GcUser.builder()
                        .userId(user.id)
                        .userStatus(UserStatus.ACTIVE)
                        .userRole(user.role)
                        .username(user.username)
                        .password(passwordEncoder.encode(user.password))
                        .profile(new UserProfile(
                                user.id, user.username + "@gc.mail", "Taipei", Instant.now(), user.recommenderId))
                        .build())
                .toList();
        var users = userRepository.saveAll(gcUsers);
        for (GcUser user : users) {
            var order = new InsuranceOrder(user, insurances.get(new Random().nextInt(3)));
            insuranceOrderRepository.save(order);
        }
    }
}
