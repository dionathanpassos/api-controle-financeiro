package com.devdion.controlefinanceiro.service;

import com.devdion.controlefinanceiro.dto.creditCardTransaction.CreditCardTransactionFilterDTO;
import com.devdion.controlefinanceiro.dto.creditCardTransaction.CreditCardTransactionRequestDTO;
import com.devdion.controlefinanceiro.dto.creditCardTransaction.CreditCardTransactionResponseDTO;
import com.devdion.controlefinanceiro.exception.BusinessException;
import com.devdion.controlefinanceiro.exception.ResourceNotFoundException;
import com.devdion.controlefinanceiro.mapper.CreditCardTransactionMapper;
import com.devdion.controlefinanceiro.model.*;
import com.devdion.controlefinanceiro.repository.CategoryRepository;
import com.devdion.controlefinanceiro.repository.CreditCardInvoiceRepository;
import com.devdion.controlefinanceiro.repository.CreditCardRepository;
import com.devdion.controlefinanceiro.repository.CreditCardTransactionRepository;
import com.devdion.controlefinanceiro.specification.CreditCardTransactionSpecification;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class CreditCardTransactionService {

    private final CreditCardTransactionRepository creditCardTransactionRepository;
    private final CreditCardInvoiceRepository creditCardInvoiceRepository;
    private final CreditCardRepository creditCardRepository;
    private final UserContextService userContextService;
    private final CreditCardInvoiceService creditCardInvoiceService;
    private final CreditCardTransactionMapper creditCardTransactionMapper;
    private final CategoryRepository categoryRepository;

    public CreditCardTransactionService(CreditCardTransactionRepository creditCardTransactionRepository, CreditCardInvoiceRepository creditCardInvoiceRepository, CreditCardRepository creditCardRepository, UserContextService userContextService, CreditCardInvoiceService creditCardInvoiceService, CreditCardTransactionMapper creditCardTransactionMapper, CategoryRepository categoryRepository) {
        this.creditCardTransactionRepository = creditCardTransactionRepository;
        this.creditCardInvoiceRepository = creditCardInvoiceRepository;
        this.creditCardRepository = creditCardRepository;
        this.userContextService = userContextService;
        this.creditCardInvoiceService = creditCardInvoiceService;
        this.creditCardTransactionMapper = creditCardTransactionMapper;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public List<CreditCardTransactionResponseDTO> create(CreditCardTransactionRequestDTO request) {
        User user = userContextService.getCurrentUser();

        CreditCard creditCard = creditCardRepository.findByIdAndUser(request.cardId(), user)
                .orElseThrow(() -> new ResourceNotFoundException("Cartão "));

        Category category = categoryRepository.findByIdAndUser(request.categoryId(), user)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria "));

        if(category.getType() == CategoryType.INCOME) {
            throw new BusinessException("Categoria precisa ser do tipo DESPESA");
        }

        if(creditCard.getStatus() != CreditCardStatus.ACTIVE) {
            throw new BusinessException("Cartão bloqueado ou cancelado");
        }

        int installments = request.installments() == null || request.installments() < 1
                ? 1
                :request.installments();

        BigDecimal installmentAmount = request.totalAmount().divide(BigDecimal.valueOf(installments),  RoundingMode.HALF_UP);

        YearMonth baseMonth =
                request.purchaseDate().getDayOfMonth() <= creditCard.getClosingDay()
                        ? YearMonth.from(request.purchaseDate())
                        : YearMonth.from(request.purchaseDate()).plusMonths(1);

        List<CreditCardTransactionResponseDTO> responseList = new ArrayList<>();


        for (int i = 0; i < installments; i++) {
            YearMonth installmentMonth = baseMonth.plusMonths(i);
            LocalDate referenceMonth = installmentMonth.atDay(1);

            CreditCardInvoice invoice = creditCardInvoiceService.findOrCreate(creditCard, referenceMonth);

            CreditCardTransaction transaction = creditCardTransactionMapper.toEntity(
                    request, user, installmentAmount, i+1, installments, invoice, category);

            CreditCardTransaction saved = creditCardTransactionRepository.save(transaction);

            responseList.add(creditCardTransactionMapper.fromEntity(saved));
        }

        return responseList;

    }

    public List<CreditCardTransactionResponseDTO> findWithFilters(
            CreditCardTransactionFilterDTO filter
    ) {
        User user = userContextService.getCurrentUser();

        Specification<CreditCardTransaction> specification = CreditCardTransactionSpecification.withFilters(
                user,
                filter.categoryId(),
                filter.description(),
                filter.creditCardId(),
                filter.startDate(),
                filter.endDate(),
                filter.invoiceId()
        );

        List<CreditCardTransaction> transactions = creditCardTransactionRepository.findAll(specification);

        return creditCardTransactionMapper.fromEntity(transactions);
    }
}
