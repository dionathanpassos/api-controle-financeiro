package com.devdion.controlefinanceiro.service;

import com.devdion.controlefinanceiro.model.User;
import com.devdion.controlefinanceiro.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserContextService {

    private final UserRepository userRepository;

    public UserContextService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // TEMPORÁRIO – depois vira SecurityContext
    public User getCurrentUser() {
        return userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Mock user not found"));
    }
}