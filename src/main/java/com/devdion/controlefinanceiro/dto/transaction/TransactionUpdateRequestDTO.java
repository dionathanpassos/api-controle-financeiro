package com.devdion.controlefinanceiro.dto.transaction;

import com.devdion.controlefinanceiro.model.TransactionType;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionUpdateRequestDTO(

        @NotNull
        Long accountId,

        @NotNull
        Long categoryId,

        @NotNull
        BigDecimal amount,

        @NotNull
        TransactionType type,

        String description,

        @NotNull
        LocalDate date
) {
}
