package com.devdion.controlefinanceiro.repository;

import com.devdion.controlefinanceiro.model.Transaction;
import com.devdion.controlefinanceiro.model.TransactionStatus;
import com.devdion.controlefinanceiro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Transaction findByIdAndUser(Long id, User user);

    List<Transaction> findAllByUserOrderByDateDesc(User user);

    Optional<Transaction> findByIdAndUserAndStatus(Long id, User user, TransactionStatus transactionStatus);
}
