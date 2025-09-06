package com.lukaszszumiec.recurring_payments_api.application.impl;

import com.lukaszszumiec.recurring_payments_api.domain.model.User;
import com.lukaszszumiec.recurring_payments_api.infrastructure.adapter.UserRepositoryAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepositoryAdapter userRepository;

    public UserDetailsServiceImpl(UserRepositoryAdapter userRepository) {
        this.userRepository = userRepository;
    }

    public UserDetails loadByEmail(String email) {
        var u = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        return org.springframework.security.core.userdetails.User
                .withUsername(u.getEmail())
                .password(u.getPassword())
                .authorities("ROLE_USER")
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return loadByEmail(username);
    }

    private UserDetails mapToSpringUser(User u) {
        return org.springframework.security.core.userdetails.User
                .withUsername(u.getEmail())
                .password(u.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_USER")))
                .build();
    }
}