package com.devdion.controlefinanceiro.mapper;

import com.devdion.controlefinanceiro.dto.transaction.TransactionRequestDTO;
import com.devdion.controlefinanceiro.dto.transaction.TransactionResponseDTO;
import com.devdion.controlefinanceiro.model.Account;
import com.devdion.controlefinanceiro.model.Category;
import com.devdion.controlefinanceiro.model.Transaction;
import com.devdion.controlefinanceiro.model.User;
import org.springframework.stereotype.Component;

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

    public TransactionResponseDTO fromEntity(Transaction transaction) {
        return new TransactionResponseDTO(
                transaction.getId(),
                transaction.getAccount().getId(),
                transaction.getCategory().getId(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getDescription(),
                transaction.getDate()
        );
    }
}
