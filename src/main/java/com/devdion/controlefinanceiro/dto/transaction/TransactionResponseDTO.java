package com.devdion.controlefinanceiro.dto.transaction;

import com.devdion.controlefinanceiro.model.TransactionStatus;
import com.devdion.controlefinanceiro.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record TransactionResponseDTO(
        Long id,
        Long accountId,
        Long categoryId,
        BigDecimal amount,
        TransactionType type,
        String description,
        LocalDate date,
        TransactionStatus status,
        LocalDateTime deletedAt
) {
}
