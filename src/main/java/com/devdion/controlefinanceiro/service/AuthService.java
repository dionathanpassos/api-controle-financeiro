package com.devdion.controlefinanceiro.service;

import com.devdion.controlefinanceiro.dto.RegisterRequestDTO;
import com.devdion.controlefinanceiro.model.User;
import com.devdion.controlefinanceiro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;



    public void register(RegisterRequestDTO request) {
        validate(request);

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(request.password());

        userRepository.save(user);
    }


    public void validate(RegisterRequestDTO request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        if (request.email() == null || request.email().isBlank()) {
            throw new IllegalArgumentException("Email é obrigatório");
        }
        if (request.name() == null || request.name().isBlank()) {
            throw new IllegalArgumentException("Name é obrigatório");
        }

    }


}
