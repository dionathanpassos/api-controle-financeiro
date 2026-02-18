package com.devdion.controlefinanceiro.dto.creditCardTransaction;

import java.time.LocalDate;

public record CreditCardTransactionFilterDTO(
        Long categoryId,
        String description,
        Long creditCardId,
        LocalDate startDate,
        LocalDate endDate,
        Long invoiceId
) {
}
