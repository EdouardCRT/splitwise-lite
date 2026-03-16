package com.edouard.splitwise_lite.service;

import com.edouard.splitwise_lite.entity.User;
import com.edouard.splitwise_lite.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(String email, String displayName, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email déjà utilisé");
        }

        User user = User.builder()
                .email(email)
                .displayName(displayName)
                .password(passwordEncoder.encode(password))
                .build();

        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}