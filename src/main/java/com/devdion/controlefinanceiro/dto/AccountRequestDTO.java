package com.devdion.controlefinanceiro.dto;

import com.devdion.controlefinanceiro.model.AccountType;
import com.devdion.controlefinanceiro.model.Institution;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record AccountRequestDTO(

        @NotBlank(message = "O nome da conta é obrigatório.")
        @Size(min = 3, max = 20, message = "O nome deve conter entre 3 e 20 caracteres.")
        String name,

        @NotNull(message = "Instituição é obrigatória.")
        Institution institution,

        @NotNull(message = "Tipo da conta é obrigatório")
        AccountType type,

        @NotNull(message = "Saldo inicial é obrigatório.")
        @DecimalMin(value = "0.00", message = "Saldo inicial não pode ser negativo.")
        BigDecimal initialBalance
) {
}
