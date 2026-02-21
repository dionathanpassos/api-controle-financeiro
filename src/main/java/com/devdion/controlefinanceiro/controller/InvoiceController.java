package com.devdion.controlefinanceiro.controller;

import com.devdion.controlefinanceiro.dto.creditCardInvoice.CreditCardInvoiceDTO;
import com.devdion.controlefinanceiro.service.CreditCardInvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final CreditCardInvoiceService invoiceService;

    public InvoiceController(CreditCardInvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public ResponseEntity<List<CreditCardInvoiceDTO>> findAllByUser() {
        return ResponseEntity.ok(invoiceService.findAll());
    }
}
