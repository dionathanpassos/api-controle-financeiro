package com.devdion.controlefinanceiro.repository;

import com.devdion.controlefinanceiro.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByNameAndInstitutionAndUserAndType(
            String name,
            Institution institution,
            User user,
            AccountType type
    );

    Optional<Account> findByIdAndUser(Long id, User user);
}
