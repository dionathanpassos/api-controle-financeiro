package com.devdion.controlefinanceiro.service;

import com.devdion.controlefinanceiro.dto.account.AccountRequestDTO;
import com.devdion.controlefinanceiro.dto.account.AccountResponseDTO;
import com.devdion.controlefinanceiro.dto.account.AccountUpdateRequestDTO;
import com.devdion.controlefinanceiro.exception.BusinessException;
import com.devdion.controlefinanceiro.exception.ResourceAlreadyExistsException;
import com.devdion.controlefinanceiro.exception.ResourceNotFoundException;
import com.devdion.controlefinanceiro.mapper.AccountMapper;
import com.devdion.controlefinanceiro.model.Account;
import com.devdion.controlefinanceiro.model.AccountStatus;
import com.devdion.controlefinanceiro.model.User;
import com.devdion.controlefinanceiro.repository.AccountRepository;
import com.devdion.controlefinanceiro.security.AuthenticatedUserService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AuthenticatedUserService authenticatedUserService;
    private final AccountMapper accountMapper;

    public AccountService(AccountRepository accountRepository,
                          AuthenticatedUserService authenticatedUserService,
                          AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.authenticatedUserService = authenticatedUserService;
        this.accountMapper = accountMapper;
    }

    public AccountResponseDTO create(AccountRequestDTO request) {
        User user = authenticatedUserService.getAuthenticatedUser();

        if (accountRepository.existsByNameAndInstitutionAndUserAndType(request.name(), request.institution(), user,request.type())) {
            throw new ResourceAlreadyExistsException("Já existe uma conta com essa configuração");
        }

        Account account = accountMapper.toEntity(request, user) ;
        Account saved = accountRepository.save(account);

        return accountMapper.fromEntity(saved);
    }

    public List<AccountResponseDTO> findAll() {
        User user = authenticatedUserService.getAuthenticatedUser();

        List<Account> accounts = accountRepository.findAllByUser(user);
        return accountMapper.fromEntity(accounts);
    }

    public AccountResponseDTO findById(Long id) {
        User user = authenticatedUserService.getAuthenticatedUser();

        Account account = accountRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Conta "));

        return accountMapper.fromEntity(account);
    }

    public AccountResponseDTO update(Long id, AccountUpdateRequestDTO request) {
        User user = authenticatedUserService.getAuthenticatedUser();

        Account account = accountRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Conta "));

        if (account.getStatus() == AccountStatus.DELETED) {
            throw new BusinessException("Conta bloqueada, não é possível alterar");
        }

        BigDecimal newInitialBalace = request.initialBalance();

        updateInitialBalance(account, newInitialBalace);

        accountMapper.updateToEntity(request, account);
        Account saved = accountRepository.save(account);

        return accountMapper.fromEntity(saved);
    }

    public AccountResponseDTO deactivate(Long id) {
        User user = authenticatedUserService.getAuthenticatedUser();

        Account account = accountRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Conta "));

        if (account.getStatus() == AccountStatus.DELETED) {
            throw new BusinessException("A conta já está desativada");
        }

        account.setStatus(AccountStatus.DELETED);
        account.setDeletedAt(LocalDateTime.now());

        Account saved = accountRepository.save(account);
        return accountMapper.fromEntity(saved);
    }

    public AccountResponseDTO activate(Long id) {
        User user = authenticatedUserService.getAuthenticatedUser();

        Account account = accountRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Conta "));

        if (account.getStatus() == AccountStatus.ACTIVE) {
            throw new BusinessException("A conta já está desativada");
        }

        account.setStatus(AccountStatus.ACTIVE);
        account.setDeletedAt(null);

        Account saved = accountRepository.save(account);
        return accountMapper.fromEntity(saved);
    }

    private void updateInitialBalance(Account account, BigDecimal newInitialBalance) {
        if (newInitialBalance.compareTo(account.getInitialBalance()) == 0) return;

        BigDecimal newBalance = account.getBalance()
                .subtract(account.getInitialBalance())
                .add(newInitialBalance);

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(
                    "A atualização do saldo inicial não pode deixar a conta negativa"
            );
        }
        account.setBalance(newBalance);
    }

}


