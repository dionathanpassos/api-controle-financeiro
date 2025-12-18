package com.devdion.controlefinanceiro.dto;

import com.devdion.controlefinanceiro.model.CategoryStatus;
import com.devdion.controlefinanceiro.model.CategoryType;

import java.time.LocalDateTime;

public record CategoryResponseDTO(
        Long id,
        String name,
        CategoryType type,
        Long parentId,
        CategoryStatus status,
        LocalDateTime deletedAt
) {
}
