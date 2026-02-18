package com.devdion.controlefinanceiro.controller;

import com.devdion.controlefinanceiro.dto.creditCard.CreditCardRequestDTO;
import com.devdion.controlefinanceiro.dto.creditCard.CreditCardResponseDTO;
import com.devdion.controlefinanceiro.dto.creditCard.CreditCardUpdateRequestDTO;
import com.devdion.controlefinanceiro.service.CreditCardService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/creditCard")
public class CreditCardController {

    private final CreditCardService creditCardService;

    public CreditCardController(CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }

    @GetMapping
    public ResponseEntity<List<CreditCardResponseDTO>> findAllByUser() {
        return ResponseEntity.ok(creditCardService.findAll());
    }

    @PostMapping
    public ResponseEntity<CreditCardResponseDTO> create(@RequestBody @Valid CreditCardRequestDTO request) {
        CreditCardResponseDTO created = creditCardService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CreditCardResponseDTO> update(@PathVariable Long id, @Valid @RequestBody CreditCardUpdateRequestDTO request) {
        CreditCardResponseDTO response = creditCardService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<CreditCardResponseDTO> deactivate(@PathVariable Long id) {
        return ResponseEntity.ok(creditCardService.deactivate(id));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<CreditCardResponseDTO> activate(@PathVariable Long id) {
        return ResponseEntity.ok(creditCardService.activate(id));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<CreditCardResponseDTO> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(creditCardService.cancel(id));
    }
}
