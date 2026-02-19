package com.devdion.controlefinanceiro.dto.auth;

public record LoginRequestDTO(
        String email,
        String password
) {
}
