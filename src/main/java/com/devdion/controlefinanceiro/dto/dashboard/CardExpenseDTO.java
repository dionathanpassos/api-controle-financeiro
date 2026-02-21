package com.devdion.controlefinanceiro.dto.dashboard;

import java.math.BigDecimal;

public record CardExpenseDTO(
        String month,
        BigDecimal expense
) {
}
