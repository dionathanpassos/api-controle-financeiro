package com.devdion.controlefinanceiro.dto.dashboard;

import java.math.BigDecimal;

public record SumByCategoryDTO(
        Long categoryId,
        BigDecimal total
) {
}
