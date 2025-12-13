package com.devdion.controlefinanceiro.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransferRequestDTO(
        Long fromAccountId,
        Long toAccountId,
        BigDecimal amount,
        String description,
        LocalDate date
) {
}
