package com.jameswu.security.demo.service;

import com.jameswu.security.demo.model.GcUser;
import com.jameswu.security.demo.model.UserAuthority;
import com.jameswu.security.demo.model.UserPayload;
import com.jameswu.security.demo.model.UserRole;
import com.jameswu.security.demo.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserManagementService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void addUser(UserPayload userPayload) {
        if (userRepository.findByUsername(userPayload.getUsername()) != null) {
            throw new IllegalArgumentException("User already exists");
        }
        GcUser newUser = GcUser.builder()
                .username(userPayload.getUsername())
                .password(bCryptPasswordEncoder.encode(userPayload.getPassword()))
                .userRole(UserRole.USER)
                .authorities(List.of(UserAuthority.USER))
                .build();
        userRepository.save(newUser);
    }

    public Iterable<GcUser> allUsers() {
        return userRepository.findAll();
    }
}
