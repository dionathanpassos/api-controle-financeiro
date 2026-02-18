package com.devdion.controlefinanceiro.dto.creditCard;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreditCardUpdateRequestDTO(
        @NotNull(message = "Nome é obriatório")
        String name,

        @NotNull(message = "Limite é obriatório")
        BigDecimal creditLimit,

        @Min(value = 1, message = "Dia de fechamento deve ser entre 1 e 31")
        @Max(value = 31, message = "Dia de vencimento deve ser entre 1 e 31")
        Integer closingDay,

        @Min(value = 1, message = "Dia de fechamento deve ser entre 1 e 31")
        @Max(value = 31, message = "Dia de vencimento deve ser entre 1 e 31")
        Integer dueDay
) {
}
