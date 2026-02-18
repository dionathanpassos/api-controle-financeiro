package com.devdion.controlefinanceiro.mapper;

import com.devdion.controlefinanceiro.dto.creditCardTransaction.CreditCardTransactionRequestDTO;
import com.devdion.controlefinanceiro.dto.creditCardTransaction.CreditCardTransactionResponseDTO;
import com.devdion.controlefinanceiro.model.Category;
import com.devdion.controlefinanceiro.model.CreditCardInvoice;
import com.devdion.controlefinanceiro.model.CreditCardTransaction;
import com.devdion.controlefinanceiro.model.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CreditCardTransactionMapper {

    public CreditCardTransaction toEntity(
            CreditCardTransactionRequestDTO request,
            User user,
            BigDecimal installmentAmount,
            int installmentNumber,
            int installments,
            CreditCardInvoice invoice,
            Category category


    ) {
        CreditCardTransaction creditCardTransaction = new CreditCardTransaction();

        creditCardTransaction.setDescription(request.description());
        creditCardTransaction.setTotalAmount(request.totalAmount());
        creditCardTransaction.setPurchaseDate(request.purchaseDate());
        creditCardTransaction.setInstallments(installments);
        creditCardTransaction.setInstallmentNumber(installmentNumber);
        creditCardTransaction.setInstallmentAmount(installmentAmount);
        creditCardTransaction.setInvoice(invoice);
        creditCardTransaction.setCategory(category);
        creditCardTransaction.setUser(user);

        return creditCardTransaction;
    }

    public CreditCardTransactionResponseDTO fromEntity(CreditCardTransaction creditCardTransaction) {
        return new CreditCardTransactionResponseDTO(
                creditCardTransaction.getId(),
                creditCardTransaction.getDescription(),
                creditCardTransaction.getTotalAmount(),
                creditCardTransaction.getInstallments(),
                creditCardTransaction.getPurchaseDate(),
                creditCardTransaction.getInstallmentNumber(),
                creditCardTransaction.getInstallmentAmount(),
                creditCardTransaction.getInvoice().getId(),
                creditCardTransaction.getInvoice().getCreditCard().getName(),
                creditCardTransaction.getCategory().getId(),
                creditCardTransaction.getDeletedAt()
        );
    }

    public List<CreditCardTransactionResponseDTO> fromEntity(List<CreditCardTransaction> creditCardTransactions) {
        return creditCardTransactions.stream()
                .map(this::fromEntity)
                .toList();
    }
}
