package com.devdion.controlefinanceiro.repository;

import com.devdion.controlefinanceiro.dto.account.AccountBalanceDTO;
import com.devdion.controlefinanceiro.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByNameAndInstitutionAndUserAndType(
            String name,
            Institution institution,
            User user,
            AccountType type
    );

    Optional<Account> findByIdAndUser(Long id, User user);

    List<Account> findAllByUser(User user);

    @Query("""
            select new com.devdion.controlefinanceiro.dto.account.AccountBalanceDTO(
            a.institution,
            a.balance
            )
            from Account a
            where a.user = :user
                and a.status = 'ACTIVE'
            """)
    List<AccountBalanceDTO> balanceByAccount(User user);

    @Query("""
            select coalesce(sum(a.balance), 0)
            from Account a
            where a.user = :user
                and a.status = 'ACTIVE'
            """)
    BigDecimal balanceTotal(User user, TransactionStatus status);
}
