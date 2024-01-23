package com.jameswu.security.demo.config;

import com.jameswu.security.demo.model.GcUser;
import com.jameswu.security.demo.model.UserProfile;
import com.jameswu.security.demo.model.UserRole;
import com.jameswu.security.demo.model.UserStatus;
import com.jameswu.security.demo.repository.UserRepository;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserInitiationConfig implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserInitiationConfig(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        UUID userId = UUID.randomUUID();
        String username = "androidx";
        String password = passwordEncoder.encode(username);
        UserProfile profile = new UserProfile(userId, "123@and.tail.com", "Taiwan", LocalDate.now(), null);
        GcUser user = new GcUser(userId, username, password, UserRole.ADMIN, profile, UserStatus.ACTIVE);
        userRepository.save(user);
    }
}
