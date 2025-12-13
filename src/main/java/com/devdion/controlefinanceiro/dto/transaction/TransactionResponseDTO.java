package com.devdion.controlefinanceiro.dto;

import com.devdion.controlefinanceiro.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionResponseDTO(
        Long id,
        Long accountId,
        Long categoryId,
        BigDecimal amount,
        TransactionType type,
        String description,
        LocalDate date
) {
}
