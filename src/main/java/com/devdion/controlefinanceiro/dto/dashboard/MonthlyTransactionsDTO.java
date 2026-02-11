package com.devdion.controlefinanceiro.dto.dashboard;

import com.devdion.controlefinanceiro.model.TransactionType;

import java.math.BigDecimal;

public record MonthlyTransactionsDTO(
        int month,
        BigDecimal income,
        BigDecimal expense
) {
}
