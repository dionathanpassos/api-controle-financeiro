package com.devdion.controlefinanceiro.controller;

import com.devdion.controlefinanceiro.dto.account.AccountRequestDTO;
import com.devdion.controlefinanceiro.dto.account.AccountResponseDTO;
import com.devdion.controlefinanceiro.dto.account.AccountUpdateRequestDTO;
import com.devdion.controlefinanceiro.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<List<AccountResponseDTO>> findAllByUser() {
        return ResponseEntity.ok(accountService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.findById(id));
    }

    @PostMapping
    public ResponseEntity<AccountResponseDTO> create(@RequestBody @Valid AccountRequestDTO request) {
        AccountResponseDTO created = accountService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> update(@PathVariable Long id, @Valid @RequestBody AccountUpdateRequestDTO request) {
        AccountResponseDTO response = accountService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<AccountResponseDTO> deactivate(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.deactivate(id));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<AccountResponseDTO> activate(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.activate(id));
    }



}
