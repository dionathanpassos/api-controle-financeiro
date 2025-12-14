package com.devdion.controlefinanceiro.service;

import com.devdion.controlefinanceiro.dto.transaction.TransactionRequestDTO;
import com.devdion.controlefinanceiro.dto.transaction.TransactionResponseDTO;
import com.devdion.controlefinanceiro.dto.transaction.TransactionUpdateRequestDTO;
import com.devdion.controlefinanceiro.dto.transaction.TransferRequestDTO;
import com.devdion.controlefinanceiro.mapper.TransactionMapper;
import com.devdion.controlefinanceiro.mapper.TransferMapper;
import com.devdion.controlefinanceiro.model.*;
import com.devdion.controlefinanceiro.repository.AccountRepository;
import com.devdion.controlefinanceiro.repository.CategoryRepositoy;
import com.devdion.controlefinanceiro.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.math.BigDecimal;

@Service
public class TransactionService {

    private final UserContextService userContextService;
    private final AccountRepository accountRepository;
    private final CategoryRepositoy categoryRepositoy;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final TransferMapper transferMapper;


    public TransactionService(UserContextService userContextService, AccountRepository accountRepository, CategoryRepositoy categoryRepositoy, TransactionRepository transactionRepository, TransactionMapper transactionMapper, TransferMapper transferMapper) {
        this.userContextService = userContextService;
        this.accountRepository = accountRepository;
        this.categoryRepositoy = categoryRepositoy;
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
        this.transferMapper = transferMapper;
    }

    @Transactional
    public TransactionResponseDTO create(TransactionRequestDTO request) {
        User user = userContextService.getCurrentUser();

        Account account = accountRepository.findByIdAndUser(request.accountId(), user).orElseThrow(() -> new RuntimeException("Conta não encontrada."));
        Category category = categoryRepositoy.findByIdAndUser(request.categoryId(), user).orElseThrow(() -> new RuntimeException("Categoria não encontrada."));

        validateCategory(request.type(), category);

        Transaction transaction = transactionMapper.toEntity(request, user, account, category);
        applyBalance(account, transaction);

        Transaction saved = transactionRepository.save(transaction);


        return transactionMapper.fromEntity(saved);

    }

    @Transactional
    public void transfer(TransferRequestDTO request) {
        User user = userContextService.getCurrentUser();


        Account from = accountRepository.findByIdAndUser(request.fromAccountId(), user).orElseThrow(() -> new RuntimeException("Conta origem não encontrada."));
        Account to = accountRepository.findByIdAndUser(request.toAccountId(), user).orElseThrow(() -> new RuntimeException("Conta destino não encontrada."));

        if (from.getId().equals(to.getId())) {
            throw new IllegalArgumentException("Conta origem e destino devem ser diferentes.");
        }

        Transaction out = transferMapper.toEntityOut(request, user, from);
        Transaction savedOut = transactionRepository.save(out);

        Transaction in = transferMapper.toEntityIn(request, user, to);
        Transaction savedIn = transactionRepository.save(in);

        savedOut.setTransferRefId(savedIn.getId());
    }

    @Transactional
    public TransactionResponseDTO update(Long id, TransactionUpdateRequestDTO request) {
        User user = userContextService.getCurrentUser();

        Transaction transaction = transactionRepository.findByIdAndUser(id, user);
        System.out.println("Chegou" + transaction.getAccount());
        System.out.println("Chegou" + transaction.getCategory());

        Account oldAccount = transaction.getAccount();
        BigDecimal oldAmount = transaction.getAmount();

        revertBalance(oldAccount, transaction);

        //Verifica se a conta foi alterada, confirma se a enviada existe no banco junto com o id do user
        if (request.accountId() != null && !request.accountId().equals(oldAccount)) {
            Account newAccount = accountRepository.findByIdAndUser(request.accountId(), user)
                    .orElseThrow(() -> new RuntimeException("Conta não encontrada."));
            transaction.setAccount(newAccount);
        }

        // Verifica se a categoria foi alterada e confirma se a enviada existe no banco.
        if (request.categoryId() != null) {
            Category category = categoryRepositoy.findByIdAndUser(request.categoryId(), user)
                    .orElseThrow(() -> new RuntimeException("Categoria não encontrada."));
            transaction.setCategory(category);
        }

        if (request.amount() != null) {
            transaction.setAmount(request.amount());
        }

        if (request.date() != null) {
            transaction.setDate(request.date());
        }

        if (request.description() != null) {
            transaction.setDescription(request.description());
        }

        if (request.type() != null) {
            transaction.setType(request.type());
        }

        System.out.println(transaction.getCategory());
        validateCategory(transaction.getType(), transaction.getCategory());
        applyBalance(transaction.getAccount(), transaction);

        Transaction saved = transactionRepository.save(transaction);

        return transactionMapper.fromEntity(saved);
    }

    private void validateCategory(TransactionType type, Category category) {
        if ((type == TransactionType.INCOME || type == TransactionType.EXPENSE) && category == null) {
            throw new IllegalArgumentException("Categoria obrigatória.");
        }

        if ((type == TransactionType.TRANSFER_IN || type == TransactionType.TRANSFER_OUT) && category != null) {
            throw new IllegalArgumentException("Transferência não usa categoria");
        }
    }


    private void revertBalance(Account account, Transaction t) {
        if (t.getType() == TransactionType.INCOME) {
            account.setBalance(account.getBalance().subtract(t.getAmount()));
        } else if (t.getType() == TransactionType.EXPENSE) {
            account.setBalance(account.getBalance().add(t.getAmount()));
        }
    }

    private void applyBalance(Account account, Transaction transaction) {

        if (transaction.getType() == TransactionType.INCOME) {
            account.setBalance(account.getBalance().add(transaction.getAmount()));
        } else if (transaction.getType() == TransactionType.EXPENSE) {
            if (account.getBalance().compareTo(transaction.getAmount()) < 0) {
                throw new RuntimeException("Saldo insuficiente");
            }
            account.setBalance(account.getBalance().subtract(transaction.getAmount()));
        }
    }
}
