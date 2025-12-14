package com.devdion.controlefinanceiro.dto.transaction;

import com.devdion.controlefinanceiro.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransferResponseDTO(
        Long id,
        Long accountId,
        TransactionType type,
        BigDecimal amount,
        String description,
        LocalDate date
) {
}
