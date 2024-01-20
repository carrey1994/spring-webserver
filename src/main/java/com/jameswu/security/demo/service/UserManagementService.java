package com.jameswu.security.demo.service;

import com.jameswu.security.demo.model.GcUser;
import com.jameswu.security.demo.model.UserPayload;
import com.jameswu.security.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserManagementService {

  @Autowired private UserRepository userRepository;

  @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;

  public void addUser(UserPayload userPayload) {
    GcUser newUser =
        GcUser.builder()
            .username(userPayload.getUsername())
            .password(bCryptPasswordEncoder.encode(userPayload.getPassword()))
            .build();
    userRepository.save(newUser);
  }
}
