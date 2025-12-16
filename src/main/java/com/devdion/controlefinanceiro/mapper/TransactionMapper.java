package com.devdion.controlefinanceiro.mapper;

import com.devdion.controlefinanceiro.dto.transaction.TransactionRequestDTO;
import com.devdion.controlefinanceiro.dto.transaction.TransactionResponseDTO;
import com.devdion.controlefinanceiro.dto.transaction.TransactionUpdateRequestDTO;
import com.devdion.controlefinanceiro.model.Account;
import com.devdion.controlefinanceiro.model.Category;
import com.devdion.controlefinanceiro.model.Transaction;
import com.devdion.controlefinanceiro.model.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TransactionMapper {

    public Transaction toEntity(TransactionRequestDTO request, User user, Account account, Category category) {
        Transaction transaction = new Transaction();

        transaction.setAccount(account);
        transaction.setCategory(category);
        transaction.setAmount(request.amount());
        transaction.setType(request.type());
        transaction.setDescription(request.description());
        transaction.setDate(request.date());
        transaction.setUser(user);

        return transaction;
    }

    public void updateEntityFromRequest(
            TransactionUpdateRequestDTO request,
            Transaction transaction
    ) {
        transaction.setAmount(request.amount());
        transaction.setDate(request.date());
        transaction.setDescription(request.description());
        transaction.setType(request.type());
    }

    public TransactionResponseDTO fromEntity(Transaction transaction) {
        return new TransactionResponseDTO(
                transaction.getId(),
                transaction.getAccount() != null ? transaction.getAccount().getId() : null,
                transaction.getCategory() != null ? transaction.getCategory().getId() : null,
                transaction.getAmount(),
                transaction.getType(),
                transaction.getDescription(),
                transaction.getDate()
        );
    }

    public List<TransactionResponseDTO> fromEntity(List<Transaction> transactions) {
        return transactions.stream()
                .map(this::fromEntity)
                .toList();
    }
}
