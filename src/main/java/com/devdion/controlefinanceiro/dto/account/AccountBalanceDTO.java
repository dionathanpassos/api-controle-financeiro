package com.devdion.controlefinanceiro.dto.account;

import com.devdion.controlefinanceiro.model.Institution;

import java.math.BigDecimal;

public record AccountBalanceDTO(
        Institution institution,
        BigDecimal balance
) {
}
