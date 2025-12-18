package com.devdion.controlefinanceiro.controller;

import com.devdion.controlefinanceiro.dto.transaction.TransactionRequestDTO;
import com.devdion.controlefinanceiro.dto.transaction.TransactionResponseDTO;
import com.devdion.controlefinanceiro.dto.transaction.TransactionUpdateRequestDTO;
import com.devdion.controlefinanceiro.dto.transaction.TransferRequestDTO;
import com.devdion.controlefinanceiro.model.TransactionType;
import com.devdion.controlefinanceiro.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

//    @GetMapping
//    public ResponseEntity<List<TransactionResponseDTO>> findAllByUser() {
//        return ResponseEntity.ok(transactionService.findAll());
//    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> filter(
            @RequestParam(required = false)TransactionType type
            ) {
        return ResponseEntity.ok(transactionService.findWithFilters(type));
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> create(@RequestBody @Valid TransactionRequestDTO request) {
        TransactionResponseDTO response = transactionService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@RequestBody @Valid TransferRequestDTO request) {
        transactionService.transfer(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> update(@PathVariable Long id, @RequestBody @Valid TransactionUpdateRequestDTO request) {
        TransactionResponseDTO response = transactionService.update(id, request);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<TransactionResponseDTO> deactivate(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.deactivate(id));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<TransactionResponseDTO> activate(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.activate(id));
    }
}
