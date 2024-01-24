package com.jameswu.security.demo.service;

import com.jameswu.security.demo.model.GcUser;
import com.jameswu.security.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class GcUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        GcUser user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("%s not found", username)));
        return User.builder()
                .username(username)
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .roles(user.getUserRole().name())
                .build();
    }
}
