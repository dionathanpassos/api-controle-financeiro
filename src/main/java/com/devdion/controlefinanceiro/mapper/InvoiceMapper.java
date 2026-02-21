package com.devdion.controlefinanceiro.mapper;

import com.devdion.controlefinanceiro.dto.creditCard.CreditCardResponseDTO;
import com.devdion.controlefinanceiro.dto.creditCardInvoice.CreditCardInvoiceDTO;
import com.devdion.controlefinanceiro.model.CreditCard;
import com.devdion.controlefinanceiro.model.CreditCardInvoice;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InvoiceMapper {

    public CreditCardInvoiceDTO fromEntity(CreditCardInvoice invoice) {
        return new CreditCardInvoiceDTO(
                invoice.getId(),
                invoice.getReferenceMonth(),
                invoice.getTotalAmount(),
                invoice.getStatus(),
                invoice.getCreditCard().getName(),
                invoice.getCreditCard().getDueDay()
        );
    }
    public List<CreditCardInvoiceDTO> fromEntity(List<CreditCardInvoice> invoices) {
        return invoices.stream()
                .map(this::fromEntity)
                .toList();

    }


}
