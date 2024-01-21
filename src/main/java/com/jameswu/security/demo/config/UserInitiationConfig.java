package com.jameswu.security.demo.config;

import com.jameswu.security.demo.model.GcUser;
import com.jameswu.security.demo.model.UserAuthority;
import com.jameswu.security.demo.model.UserRole;
import com.jameswu.security.demo.repository.UserRepository;
import java.util.List;
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
        GcUser user = new GcUser(
                UUID.randomUUID(),
                "androidx",
                passwordEncoder.encode("androidx"),
                UserRole.ADMIN,
                List.of(UserAuthority.ADMIN));
        userRepository.save(user);
    }
}
