package com.devdion.controlefinanceiro.controller;

import com.devdion.controlefinanceiro.dto.transaction.TransactionRequestDTO;
import com.devdion.controlefinanceiro.dto.transaction.TransactionResponseDTO;
import com.devdion.controlefinanceiro.dto.transaction.TransactionUpdateRequestDTO;
import com.devdion.controlefinanceiro.dto.transaction.TransferRequestDTO;
import com.devdion.controlefinanceiro.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> create(@RequestBody TransactionRequestDTO request) {
        TransactionResponseDTO response = transactionService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@RequestBody @Valid TransferRequestDTO request) {
        transactionService.transfer(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> update(@PathVariable Long id, @RequestBody TransactionUpdateRequestDTO request) {
        TransactionResponseDTO response = transactionService.update(id, request);

        return ResponseEntity.ok(response);

    }
}
