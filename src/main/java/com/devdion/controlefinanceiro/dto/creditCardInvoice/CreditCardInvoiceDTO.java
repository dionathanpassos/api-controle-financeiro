package com.devdion.controlefinanceiro.dto.creditCardInvoice;

import com.devdion.controlefinanceiro.model.InvoiceStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreditCardInvoiceDTO(
        Long id,
        LocalDate referenceMonth,
        BigDecimal totalAmount,
        InvoiceStatus status,
        String creditCard,
        Integer dueDay
) {
}
