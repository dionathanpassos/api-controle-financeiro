package com.devdion.controlefinanceiro.dto;

import com.devdion.controlefinanceiro.model.CategoryType;

public record CategoryResponseDTO(
        Long id,
        String name,
        CategoryType type,
        Long parentId
) {
}
