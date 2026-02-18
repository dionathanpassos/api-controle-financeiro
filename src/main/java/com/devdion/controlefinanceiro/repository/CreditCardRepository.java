package com.devdion.controlefinanceiro.repository;

import com.devdion.controlefinanceiro.model.CreditCard;
import com.devdion.controlefinanceiro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {
    List<CreditCard> findAllByUser(User user);

    Optional<CreditCard> findByIdAndUser(Long id, User user);
}
