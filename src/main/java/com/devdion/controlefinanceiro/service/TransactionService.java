package com.devdion.controlefinanceiro.service;

import com.devdion.controlefinanceiro.dto.TransactionRequestDTO;
import com.devdion.controlefinanceiro.mapper.TransactionMapper;
import com.devdion.controlefinanceiro.model.*;
import com.devdion.controlefinanceiro.repository.AccountRepository;
import com.devdion.controlefinanceiro.repository.CategoryRepositoy;
import com.devdion.controlefinanceiro.repository.TransactionRepository;
import org.springframework.stereotype.Service;

@Service
public class TransctionService {

    private final UserContextService userContextService;
    private final AccountRepository accountRepository;
    private final CategoryRepositoy categoryRepositoy;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;


    public TransctionService(UserContextService userContextService, AccountRepository accountRepository, CategoryRepositoy categoryRepositoy, TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.userContextService = userContextService;
        this.accountRepository = accountRepository;
        this.categoryRepositoy = categoryRepositoy;
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    public void create(TransactionRequestDTO request) {
        User user = userContextService.getCurrentUser();

        Account account = accountRepository.findById(request.accountId()).orElseThrow(() -> new RuntimeException("Conta não encontrada."));
        Category category = categoryRepositoy.findById(request.categoryId()).orElseThrow(() -> new RuntimeException("Categoria não encontrada."));

        validateCategory(request.type(), category);

        Transaction transaction = transactionMapper.toEntity(request, user, account, category);
        transactionRepository.save(transaction);

    }

    private void validateCategory(TransactionType type, Category category) {
        if ((type == TransactionType.INCOME || type == TransactionType.EXPENSE) && category == null) {
            throw new IllegalArgumentException("Categoria obrigatória.");
        }

        if ((type == TransactionType.TRANSFER_IN || type == TransactionType.TRANSFER_OUT) && category != null) {
            throw new IllegalArgumentException("Transferência não usa categoria");
        }

    }
}
