package com.devdion.controlefinanceiro.dto;

import com.devdion.controlefinanceiro.model.AccountType;
import com.devdion.controlefinanceiro.model.Institution;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record AccountRequestDTO(
        @NotBlank(message = "O nome da conta é obrigatório.")
        @Size(min = 3, max = 20, message = "O nome deve conter entre 3 e 20 caracteres.")
        String name,
        Institution institution,
        AccountType type,
        BigDecimal initialBalance
) {
}
