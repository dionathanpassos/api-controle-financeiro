package com.devdion.controlefinanceiro.dto;

import com.devdion.controlefinanceiro.model.AccountType;
import com.devdion.controlefinanceiro.model.Institution;

import java.math.BigDecimal;

public record AccountResponseDTO(
        Long id,
        String name,
        Institution institution,
        AccountType type,
        BigDecimal balance,
        BigDecimal initialBalance
) {
}
