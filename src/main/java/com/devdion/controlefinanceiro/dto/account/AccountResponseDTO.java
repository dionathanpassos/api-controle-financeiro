package com.devdion.controlefinanceiro.dto.account;

import com.devdion.controlefinanceiro.model.AccountStatus;
import com.devdion.controlefinanceiro.model.AccountType;
import com.devdion.controlefinanceiro.model.Institution;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountResponseDTO(
        Long id,
        String name,
        Institution institution,
        AccountType type,
        BigDecimal balance,
        BigDecimal initialBalance,
        AccountStatus status,
        LocalDateTime deletedAt
) {
}
