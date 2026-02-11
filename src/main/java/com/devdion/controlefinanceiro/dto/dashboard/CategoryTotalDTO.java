package com.devdion.controlefinanceiro.dto.dashboard;

import java.math.BigDecimal;
import java.util.List;

public record CategoryTotalDTO(
        Long id,
        String name,
        BigDecimal total,
        List<SubcategoryTotalDTO> subcategories
) {
}
