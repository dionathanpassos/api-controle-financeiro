package com.devdion.controlefinanceiro.dto;

import com.devdion.controlefinanceiro.model.CategoryType;

public record CategoryRequestDTO(
        String name,
        CategoryType type,
        Long parentId
) {
}
