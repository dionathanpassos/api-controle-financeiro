package com.devdion.controlefinanceiro.dto.creditCard;

import com.devdion.controlefinanceiro.model.CreditCardStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreditCardResponseDTO(
        Long id,
        String name,
        BigDecimal creditLimit,
        Integer closingDay,
        Integer dueDay,
        CreditCardStatus status,
        LocalDateTime deletedAt
) {
}
