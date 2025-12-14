package com.devdion.controlefinanceiro.dto.transaction;

import com.devdion.controlefinanceiro.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionUpdateRequestDTO(
        Long accountId,
        Long categoryId,
        BigDecimal amount,
        TransactionType type,
        String description,
        LocalDate date
) {
}
