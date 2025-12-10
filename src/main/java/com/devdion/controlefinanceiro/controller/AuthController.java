package com.devdion.controlefinanceiro.controller;

import com.devdion.controlefinanceiro.dto.RegisterRequestDTO;
import com.devdion.controlefinanceiro.model.User;
import com.devdion.controlefinanceiro.repository.UserRepository;
import com.devdion.controlefinanceiro.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequestDTO request) {
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
