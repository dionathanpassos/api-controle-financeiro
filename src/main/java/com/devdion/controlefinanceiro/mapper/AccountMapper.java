package com.devdion.controlefinanceiro.mapper;

import com.devdion.controlefinanceiro.dto.AccountRequestDTO;
import com.devdion.controlefinanceiro.dto.AccountResponseDTO;
import com.devdion.controlefinanceiro.model.Account;
import com.devdion.controlefinanceiro.model.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

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

    public AccountResponseDTO fromEntity(Account account) {
        return new AccountResponseDTO(
                account.getId(),
                account.getName(),
                account.getInstitution(),
                account.getType(),
                account.getBalance(),
                account.getInitialBalance()

        );
    }
}
