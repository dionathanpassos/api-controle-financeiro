package com.devdion.controlefinanceiro.repository;

import com.devdion.controlefinanceiro.model.Transaction;
import com.devdion.controlefinanceiro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Transaction findByIdAndUser(Long id, User user);
}
