package com.devdion.controlefinanceiro.repository;

import com.devdion.controlefinanceiro.model.CreditCardTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CreditCardTransactionRepository extends JpaRepository<CreditCardTransaction, Long>,
        JpaSpecificationExecutor<CreditCardTransaction> {
}
