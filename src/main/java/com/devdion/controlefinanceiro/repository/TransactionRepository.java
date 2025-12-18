package com.devdion.controlefinanceiro.repository;

import com.devdion.controlefinanceiro.model.Transaction;
import com.devdion.controlefinanceiro.model.TransactionStatus;
import com.devdion.controlefinanceiro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

    Optional<Transaction> findByIdAndUser(Long id, User user);

    List<Transaction> findAllByUserOrderByDateDesc(User user);

}
