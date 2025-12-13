package com.devdion.controlefinanceiro.controller;

import com.devdion.controlefinanceiro.dto.AccountRequestDTO;
import com.devdion.controlefinanceiro.dto.AccountResponseDTO;
import com.devdion.controlefinanceiro.dto.CategoryResponseDTO;
import com.devdion.controlefinanceiro.model.Account;
import com.devdion.controlefinanceiro.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountResponseDTO> create(@RequestBody @Valid AccountRequestDTO request) {
        AccountResponseDTO created = accountService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);

    }
}
