package com.devdion.controlefinanceiro.mapper;

import com.devdion.controlefinanceiro.dto.creditCard.CreditCardRequestDTO;
import com.devdion.controlefinanceiro.dto.creditCard.CreditCardResponseDTO;
import com.devdion.controlefinanceiro.dto.creditCard.CreditCardUpdateRequestDTO;
import com.devdion.controlefinanceiro.model.CreditCard;
import com.devdion.controlefinanceiro.model.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CreditCardMapper {

    public CreditCard toEntity(CreditCardRequestDTO request, User user) {
        CreditCard creditCard = new CreditCard();

        creditCard.setName(request.name());
        creditCard.setCreditLimit(request.creditLimit());
        creditCard.setClosingDay(request.closingDay());
        creditCard.setDueDay(request.dueDay());
        creditCard.setUser(user);

        return creditCard;
    }

    public CreditCardResponseDTO fromEntity(CreditCard creditCard) {
        return new CreditCardResponseDTO(
                creditCard.getId(),
                creditCard.getName(),
                creditCard.getCreditLimit(),
                creditCard.getClosingDay(),
                creditCard.getDueDay(),
                creditCard.getStatus(),
                creditCard.getDeletedAt()
        );
    }

    public List<CreditCardResponseDTO> fromEntity(List<CreditCard> creditCards) {
        return creditCards.stream()
                .map(this::fromEntity)
                .toList();
    }

    public void updateEntityFromRequest(CreditCardUpdateRequestDTO request, CreditCard creditCard) {
        creditCard.setName(request.name());
        creditCard.setCreditLimit(request.creditLimit());
        creditCard.setClosingDay(request.closingDay());
        creditCard.setDueDay(request.dueDay());
    }
}
