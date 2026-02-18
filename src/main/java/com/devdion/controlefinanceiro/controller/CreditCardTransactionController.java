package com.devdion.controlefinanceiro.controller;

import com.devdion.controlefinanceiro.dto.creditCardTransaction.CreditCardTransactionFilterDTO;
import com.devdion.controlefinanceiro.dto.creditCardTransaction.CreditCardTransactionRequestDTO;
import com.devdion.controlefinanceiro.dto.creditCardTransaction.CreditCardTransactionResponseDTO;
import com.devdion.controlefinanceiro.dto.transaction.TransactionResponseDTO;
import com.devdion.controlefinanceiro.model.TransactionType;
import com.devdion.controlefinanceiro.service.CreditCardTransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/creditCardTransaction")
public class CreditCardTransactionController {

    private final CreditCardTransactionService creditCardTransactionService;


    public CreditCardTransactionController(CreditCardTransactionService creditCardTransactionService) {
        this.creditCardTransactionService = creditCardTransactionService;
    }

    @PostMapping
    public ResponseEntity<List<CreditCardTransactionResponseDTO>> create(@RequestBody @Valid CreditCardTransactionRequestDTO request) {
        List<CreditCardTransactionResponseDTO> created = creditCardTransactionService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<CreditCardTransactionResponseDTO>> filter(
            @ModelAttribute CreditCardTransactionFilterDTO filter
    ) {
        return ResponseEntity.ok(creditCardTransactionService.findWithFilters(filter));
    }


}
