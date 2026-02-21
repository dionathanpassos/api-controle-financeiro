package com.devdion.controlefinanceiro.dto.dashboard;

import java.math.BigDecimal;

public record CategorySummaryDTO(
        String category,
        BigDecimal expense
) {
}
