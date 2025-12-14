package com.devdion.controlefinanceiro.mapper;

import com.devdion.controlefinanceiro.dto.transaction.TransferRequestDTO;
import com.devdion.controlefinanceiro.model.*;
import org.springframework.stereotype.Component;

@Component
public class TransferMapper {

    public Transaction toEntityOut(TransferRequestDTO request, User user, Account account) {
        Transaction transaction = new Transaction();

        transaction.setAccount(account);
        transaction.setAmount(request.amount());
        transaction.setType(TransactionType.TRANSFER_OUT);
        transaction.setDescription(request.description());
        transaction.setDate(request.date());
        transaction.setUser(user);

        return transaction;
    }

    public Transaction toEntityIn(TransferRequestDTO request, User user, Account account) {
        Transaction transaction = new Transaction();

        transaction.setAccount(account);
        transaction.setAmount(request.amount());
        transaction.setType(TransactionType.TRANSFER_IN);
        transaction.setDescription(request.description());
        transaction.setDate(request.date());
        transaction.setUser(user);

        return transaction;
    }
}
