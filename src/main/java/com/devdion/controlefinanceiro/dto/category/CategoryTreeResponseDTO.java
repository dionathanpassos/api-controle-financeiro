package com.devdion.controlefinanceiro.dto.category;

import com.devdion.controlefinanceiro.model.CategoryStatus;
import com.devdion.controlefinanceiro.model.CategoryType;

import java.time.LocalDateTime;
import java.util.List;

public record CategoryTreeResponseDTO(
        Long id,
        String name,
        CategoryType type,
        CategoryStatus status,
        LocalDateTime deletedAt,
        List<CategoryResponseDTO> subcategories
) {
}
