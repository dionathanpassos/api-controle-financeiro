package com.devdion.controlefinanceiro.dto.category;

import com.devdion.controlefinanceiro.model.CategoryType;
import jakarta.validation.constraints.NotNull;

public record CategoryRequestDTO(

        @NotNull
        String name,

        @NotNull
        CategoryType type,

        Long parentId
) {
}
