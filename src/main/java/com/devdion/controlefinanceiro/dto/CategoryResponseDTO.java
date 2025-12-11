package com.devdion.controlefinanceiro.dto;

import com.devdion.controlefinanceiro.model.CategoryType;

public record CategoryRequestDTO(
        Long id,
        String name,
        CategoryType type
) {
}
