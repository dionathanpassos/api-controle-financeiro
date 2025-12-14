package com.devdion.controlefinanceiro.dto.transaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransferRequestDTO(

        @NotNull(message = "Conta de origem é obrigatório.")
        Long fromAccountId,

        @NotNull(message = "Conta de destino é obrigatório.")
        Long toAccountId,

        @NotNull(message = "O valor da transferência é obrigatório.")
        BigDecimal amount,

        String description,

        @NotNull(message = "A data é obrigatório.")
        LocalDate date
) {
}
