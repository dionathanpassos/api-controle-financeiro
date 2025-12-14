package com.devdion.controlefinanceiro.dto.transaction;

import com.devdion.controlefinanceiro.model.TransactionType;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequestDTO(

        @NotNull(message = "A conta é obrigatória")
        Long accountId,

        @NotNull(message = "A categoria é obrigatória")
        Long categoryId,

        @NotNull(message = "A valor da transação é obrigatório")
        BigDecimal amount,

        @NotNull(message = "O tipo é obrigatório")
        TransactionType type,

        String description,

        @NotNull(message = "A data é obrigatória")
        LocalDate date

) {
}
