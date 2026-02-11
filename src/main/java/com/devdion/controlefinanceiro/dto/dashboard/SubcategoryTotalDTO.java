package com.devdion.controlefinanceiro.dto.dashboard;

import java.math.BigDecimal;

public record SubcategoryTotalDTO(
        Long id,
        String name,
        BigDecimal total
) {
}
