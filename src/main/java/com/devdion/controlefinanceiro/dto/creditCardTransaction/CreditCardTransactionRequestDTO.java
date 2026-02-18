package com.devdion.controlefinanceiro.dto.creditCardTransaction;

import com.devdion.controlefinanceiro.model.Category;
import com.devdion.controlefinanceiro.model.CreditCardInvoice;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreditCardTransactionRequestDTO(
        Long cardId,
        String description,
        BigDecimal totalAmount,
        LocalDate purchaseDate,
        Integer installments,
        Long categoryId

) {
}
