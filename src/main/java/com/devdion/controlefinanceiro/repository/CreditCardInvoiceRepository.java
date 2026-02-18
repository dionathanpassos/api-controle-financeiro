package com.devdion.controlefinanceiro.repository;

import com.devdion.controlefinanceiro.model.CreditCard;
import com.devdion.controlefinanceiro.model.CreditCardInvoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface CreditCardInvoiceRepository extends JpaRepository<CreditCardInvoice, Long> {

    Optional<CreditCardInvoice> findByCreditCardAndReferenceMonth(CreditCard creditCard, LocalDate referenceMonth);
}
