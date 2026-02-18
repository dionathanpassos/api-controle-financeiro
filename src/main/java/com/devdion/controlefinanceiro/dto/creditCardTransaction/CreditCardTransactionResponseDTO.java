package com.devdion.controlefinanceiro.dto.creditCardTransaction;

import com.devdion.controlefinanceiro.model.Category;
import com.devdion.controlefinanceiro.model.CreditCardInvoice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record CreditCardTransactionResponseDTO(
        Long id,
        String description,
        BigDecimal totalAmount,
        Integer installments,
        LocalDate purchaseDate,
        Integer installmentNumber,
        BigDecimal installmentAmount,
        Long invoiceId,
        String creditCardName,
        Long categoryId,
        LocalDateTime deletedAt
) {
}
