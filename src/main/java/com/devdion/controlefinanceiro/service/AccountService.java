package com.devdion.controlefinanceiro.service;

import com.devdion.controlefinanceiro.dto.AccountRequestDTO;
import com.devdion.controlefinanceiro.dto.AccountResponseDTO;
import com.devdion.controlefinanceiro.dto.CategoryResponseDTO;
import com.devdion.controlefinanceiro.mapper.AccountMapper;
import com.devdion.controlefinanceiro.model.Account;
import com.devdion.controlefinanceiro.model.Category;
import com.devdion.controlefinanceiro.model.User;
import com.devdion.controlefinanceiro.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserContextService userContextService;
    private final AccountMapper accountMapper;

    public AccountService(AccountRepository accountRepository,
                          UserContextService userContextService,
                          AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.userContextService = userContextService;
        this.accountMapper = accountMapper;
    }

    public AccountResponseDTO create(AccountRequestDTO request) {
        User user = userContextService.getCurrentUser();

        if (request.name().isBlank()) {
            throw new IllegalArgumentException("Campo");
        }

        if (accountRepository.existsByNameAndInstitutionAndUserAndType(request.name(), request.institution(), user,request.type())) {
//            throw new RuntimeException("Já existe uma conta com essa configuração");
        }


        Account account = accountMapper.toEntity(request, user) ;
        Account saved = accountRepository.save(account);

        return accountMapper.fromEntity(saved);
    }


}


