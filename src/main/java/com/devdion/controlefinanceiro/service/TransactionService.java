package com.devdion.controlefinanceiro.service;

import com.devdion.controlefinanceiro.dto.transaction.TransactionRequestDTO;
import com.devdion.controlefinanceiro.dto.transaction.TransactionResponseDTO;
import com.devdion.controlefinanceiro.dto.transaction.TransactionUpdateRequestDTO;
import com.devdion.controlefinanceiro.dto.transaction.TransferRequestDTO;
import com.devdion.controlefinanceiro.exception.BusinessException;
import com.devdion.controlefinanceiro.exception.ResourceNotFoundException;
import com.devdion.controlefinanceiro.mapper.TransactionMapper;
import com.devdion.controlefinanceiro.mapper.TransferMapper;
import com.devdion.controlefinanceiro.model.*;
import com.devdion.controlefinanceiro.repository.AccountRepository;
import com.devdion.controlefinanceiro.repository.CategoryRepository;
import com.devdion.controlefinanceiro.repository.TransactionRepository;
import com.devdion.controlefinanceiro.security.AuthenticatedUserService;
import com.devdion.controlefinanceiro.specification.TransactionSpecification;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    private final AuthenticatedUserService authenticatedUserService;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepositoy;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final TransferMapper transferMapper;


    public TransactionService(
            AuthenticatedUserService authenticatedUserService, AccountRepository accountRepository,
            CategoryRepository categoryRepositoy,
            TransactionRepository transactionRepository,
            TransactionMapper transactionMapper,
            TransferMapper transferMapper) {
        this.authenticatedUserService = authenticatedUserService;
        this.accountRepository = accountRepository;
        this.categoryRepositoy = categoryRepositoy;
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
        this.transferMapper = transferMapper;
    }

    @Transactional
    public TransactionResponseDTO create(TransactionRequestDTO request) {
        User user = authenticatedUserService.getAuthenticatedUser();

        Account account = accountRepository.findByIdAndUser(request.accountId(), user)
                .orElseThrow(() -> new ResourceNotFoundException("Conta "));

        Category category = categoryRepositoy.findByIdAndUser(request.categoryId(), user)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria "));

        if (category.getParent() == null) {
            throw new BusinessException("Escolha uma categoria válida");
        }

        Transaction transaction = transactionMapper.toEntity(request, user, account, category);

        applyBalance(account, transaction);

        Transaction saved = transactionRepository.save(transaction);

        return transactionMapper.fromEntity(saved);
    }

    @Transactional
    public void transfer(TransferRequestDTO request) {
        User user = authenticatedUserService.getAuthenticatedUser();

        Account from = accountRepository.findByIdAndUser(request.fromAccountId(), user)
                .orElseThrow(() -> new ResourceNotFoundException("Conta "));

        Account to = accountRepository.findByIdAndUser(request.toAccountId(), user)
                .orElseThrow(() -> new ResourceNotFoundException("Conta "));

        if (from.getId().equals(to.getId())) {
            throw new BusinessException("Conta origem e destino devem ser diferentes");
        }

        Transaction out = transferMapper.toEntityOut(request, user, from);
        Transaction savedOut = transactionRepository.save(out);
        applyBalance(from, out);

        Transaction in = transferMapper.toEntityIn(request, user, to);
        Transaction savedIn = transactionRepository.save(in);
        applyBalance(to, in);

        savedOut.setTransferRefId(savedIn.getId());
    }

    @Transactional
    public TransactionResponseDTO update(Long id, TransactionUpdateRequestDTO request) {
        User user = authenticatedUserService.getAuthenticatedUser();

        Transaction transaction = transactionRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Transação "));

        if (transaction.getStatus() == TransactionStatus.DELETED) {
            throw new BusinessException("Transação estornada, não é possível alterar");
        }


        revertBalance(transaction.getAccount(), transaction);

        //Verifica se a conta foi alterada, confirma se a enviada existe no banco junto com o id do user
        Account account = accountRepository.findByIdAndUser(request.accountId(), user)
                .orElseThrow(() -> new ResourceNotFoundException("Conta "));


        // Verifica se a categoria foi alterada e confirma se a enviada existe no banco.
        Category category = categoryRepositoy.findByIdAndUser(request.categoryId(), user)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria "));

        transaction.setAccount(account);
        transaction.setCategory(category);

        transactionMapper.updateEntityFromRequest(request, transaction);

        validateCategory(transaction.getType(), transaction.getCategory());

        applyBalance(transaction.getAccount(), transaction);

        Transaction saved = transactionRepository.save(transaction);

        return transactionMapper.fromEntity(saved);
    }

    @Transactional
    public TransactionResponseDTO deactivate(Long id) {
        User user = authenticatedUserService.getAuthenticatedUser();

        Transaction transaction = transactionRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Transação "));

        if (transaction.getStatus() == TransactionStatus.DELETED) {
            throw new BusinessException("A conta já está desativada");
        }
        Account account = transaction.getAccount();
        revertBalance(account, transaction);

        transaction.setStatus(TransactionStatus.DELETED);
        transaction.setDeletedAt(LocalDateTime.now());

        Transaction saved = transactionRepository.save(transaction);
        return transactionMapper.fromEntity(saved);

    }

    @Transactional
    public TransactionResponseDTO activate(Long id) {
        User user = authenticatedUserService.getAuthenticatedUser();

        Transaction transaction = transactionRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Transação "));

        if (transaction.getStatus() == TransactionStatus.ACTIVE) {
            throw new BusinessException("A conta já está ativada");
        }
        Account account = transaction.getAccount();
        restoreBalance(account, transaction);

        transaction.setStatus(TransactionStatus.ACTIVE);
        transaction.setDeletedAt(null);

        Transaction saved = transactionRepository.save(transaction);
        return transactionMapper.fromEntity(saved);

    }

    @Transactional
    public TransactionResponseDTO findById(Long id) {
        User user = authenticatedUserService.getAuthenticatedUser();

        Transaction transaction = transactionRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Transação "));
        return transactionMapper.fromEntity(transaction);

    }

    public List<TransactionResponseDTO> findWithFilters(
            TransactionType type
    ) {
        User user = authenticatedUserService.getAuthenticatedUser();

        Specification<Transaction> specification = TransactionSpecification.withFilters(type, user);

        List<Transaction> transactions = transactionRepository.findAll(specification);
        return transactionMapper.fromEntity(transactions);
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

    private void restoreBalance(Account account, Transaction t) {
        if (t.getType() == TransactionType.INCOME) {
            account.setBalance(account.getBalance().add(t.getAmount()));
        } else if (t.getType() == TransactionType.EXPENSE) {
            account.setBalance(account.getBalance().subtract(t.getAmount()));
        }
    }

    private void applyBalance(Account account, Transaction transaction) {

        if (transaction.getType() == TransactionType.INCOME || transaction.getType() == TransactionType.TRANSFER_IN) {
            account.setBalance(account.getBalance().add(transaction.getAmount()));
        } else if (transaction.getType() == TransactionType.EXPENSE || transaction.getType() == TransactionType.TRANSFER_OUT) {
            if (account.getBalance().compareTo(transaction.getAmount()) < 0) {
                throw new BusinessException("Saldo insuficiente");
            }
            account.setBalance(account.getBalance().subtract(transaction.getAmount()));
        }
    }




}
