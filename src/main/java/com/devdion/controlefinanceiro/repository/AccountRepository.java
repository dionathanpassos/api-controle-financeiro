package com.devdion.controlefinanceiro.repository;

import com.devdion.controlefinanceiro.model.Account;
import com.devdion.controlefinanceiro.model.AccountType;
import com.devdion.controlefinanceiro.model.Institution;
import com.devdion.controlefinanceiro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByNameAndInstitutionAndUserAndType(
            String name,
            Institution institution,
            User user,
            AccountType type
    );
}
