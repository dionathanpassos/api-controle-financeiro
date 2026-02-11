package com.devdion.controlefinanceiro.mapper;

import com.devdion.controlefinanceiro.dto.account.AccountRequestDTO;
import com.devdion.controlefinanceiro.dto.account.AccountResponseDTO;
import com.devdion.controlefinanceiro.dto.account.AccountUpdateRequestDTO;
import com.devdion.controlefinanceiro.model.Account;
import com.devdion.controlefinanceiro.model.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class AccountMapper {

    public Account toEntity(AccountRequestDTO request, User user) {
        BigDecimal initial = request.initialBalance() != null ? request.initialBalance() : BigDecimal.ZERO;

        Account account = new Account();

        account.setName(request.name());
        account.setType(request.type());
        account.setInstitution(request.institution());
        account.setInitialBalance(initial);
        account.setBalance(initial);
        account.setUser(user);

        return account;
    }

    public Account updateToEntity(AccountUpdateRequestDTO request, Account account) {
        BigDecimal initial = request.initialBalance() != null ? request.initialBalance() : BigDecimal.ZERO;

        account.setName(request.name());
        account.setType(request.type());
        account.setInstitution(request.institution());
        account.setInitialBalance(initial);

        return account;

    }

    public AccountResponseDTO fromEntity(Account account) {
        return new AccountResponseDTO(
                account.getId(),
                account.getName(),
                account.getInstitution(),
                account.getType(),
                account.getBalance(),
                account.getInitialBalance(),
                account.getStatus(),
                account.getDeletedAt()

        );
    }

    public List<AccountResponseDTO> fromEntity(List<Account> accounts) {
        return accounts.stream()
                .map(this::fromEntity)
                .toList();
    }
}
