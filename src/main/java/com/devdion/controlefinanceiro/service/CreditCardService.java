package com.devdion.controlefinanceiro.service;

import com.devdion.controlefinanceiro.dto.creditCard.CreditCardRequestDTO;
import com.devdion.controlefinanceiro.dto.creditCard.CreditCardResponseDTO;
import com.devdion.controlefinanceiro.dto.creditCard.CreditCardUpdateRequestDTO;
import com.devdion.controlefinanceiro.exception.BusinessException;
import com.devdion.controlefinanceiro.exception.ResourceNotFoundException;
import com.devdion.controlefinanceiro.mapper.CreditCardMapper;
import com.devdion.controlefinanceiro.model.CreditCard;
import com.devdion.controlefinanceiro.model.CreditCardStatus;
import com.devdion.controlefinanceiro.model.User;
import com.devdion.controlefinanceiro.repository.CreditCardRepository;
import com.devdion.controlefinanceiro.security.AuthenticatedUserService;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CreditCardService {
    private final CreditCardRepository creditCardRepository;
    private final CreditCardMapper creditCardMapper;
    private final AuthenticatedUserService authenticatedUserService;


    public CreditCardService(CreditCardRepository creditCardRepository,
                             CreditCardMapper creditCardMapper,
                             AuthenticatedUserService authenticatedUserService) {
        this.creditCardRepository = creditCardRepository;
        this.creditCardMapper = creditCardMapper;
        this.authenticatedUserService = authenticatedUserService;
    }

    public CreditCardResponseDTO create(CreditCardRequestDTO request) {
        User user = authenticatedUserService.getAuthenticatedUser();

        if(request.dueDay().equals(request.closingDay())) {
            throw new BusinessException("Dia de vencimento não pode ser igual ao de fechamento");
        }

        CreditCard creditCard = creditCardMapper.toEntity(request, user);

        CreditCard saved = creditCardRepository.save(creditCard);
        return creditCardMapper.fromEntity(saved);


    }

    public List<CreditCardResponseDTO> findAll() {
        User user = authenticatedUserService.getAuthenticatedUser();

        List<CreditCard> creditCards = creditCardRepository.findAllByUser(user);

        return creditCardMapper.fromEntity(creditCards);
    }

    public CreditCardResponseDTO update(Long id, CreditCardUpdateRequestDTO request) {
        User user = authenticatedUserService.getAuthenticatedUser();

        CreditCard creditCard = creditCardRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Cartão "));

        if(creditCard.getStatus() == CreditCardStatus.CANCELED) {
            throw new BusinessException("Cartão cancelado, não é possível fazer alterações");
        }

        if(request.dueDay().equals(request.closingDay())) {
            throw new BusinessException("Dia de vencimento não pode ser igual ao de fechamento");
        }

        creditCardMapper.updateEntityFromRequest(request, creditCard);
        CreditCard saved = creditCardRepository.save(creditCard);

        return creditCardMapper.fromEntity(saved);
    }

    public CreditCardResponseDTO deactivate(Long id) {
        User user = authenticatedUserService.getAuthenticatedUser();

        CreditCard creditCard = creditCardRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Cartão "));

        if(creditCard.getStatus() == CreditCardStatus.CANCELED) {
            throw new BusinessException("Cartão cancelado, não é possível mudar status");
        }

        if(creditCard.getStatus() == CreditCardStatus.BLOCKED) {
            throw new BusinessException("Cartão já bloqueado");
        }

        creditCard.setStatus(CreditCardStatus.BLOCKED);

        CreditCard saved = creditCardRepository.save(creditCard);

        return creditCardMapper.fromEntity(saved);
    }

    public CreditCardResponseDTO activate(Long id) {
        User user = authenticatedUserService.getAuthenticatedUser();

        CreditCard creditCard = creditCardRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Cartão "));

        if(creditCard.getStatus() == CreditCardStatus.CANCELED) {
            throw new BusinessException("Cartão cancelado, não é possível mudar status");
        }

        if(creditCard.getStatus() == CreditCardStatus.ACTIVE) {
            throw new BusinessException("Cartão já desativado");
        }

        creditCard.setStatus(CreditCardStatus.ACTIVE);

        CreditCard saved = creditCardRepository.save(creditCard);
        return creditCardMapper.fromEntity(saved);
    }

    public CreditCardResponseDTO cancel(Long id) {
        User user = authenticatedUserService.getAuthenticatedUser();

        CreditCard creditCard = creditCardRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Cartão "));

        if(creditCard.getStatus() == CreditCardStatus.CANCELED) {
            throw new BusinessException("Cartão já cancelado");
        }

        creditCard.setStatus(CreditCardStatus.CANCELED);

        CreditCard saved = creditCardRepository.save(creditCard);
        return creditCardMapper.fromEntity(saved);
    }
}
