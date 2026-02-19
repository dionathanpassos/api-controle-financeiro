package com.devdion.controlefinanceiro.dto.auth;

public record RegisterRequestDTO(
        String name,
        String email,
        String password
) {
}
