package com.devdion.controlefinanceiro.service;

import com.devdion.controlefinanceiro.dto.creditCardInvoice.CreditCardInvoiceDTO;
import com.devdion.controlefinanceiro.mapper.InvoiceMapper;
import com.devdion.controlefinanceiro.model.CreditCard;
import com.devdion.controlefinanceiro.model.CreditCardInvoice;
import com.devdion.controlefinanceiro.model.InvoiceStatus;
import com.devdion.controlefinanceiro.model.User;
import com.devdion.controlefinanceiro.repository.CreditCardInvoiceRepository;
import com.devdion.controlefinanceiro.security.AuthenticatedUserService;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
public class CreditCardInvoiceService {

    private final CreditCardInvoiceRepository creditCardInvoiceRepository;
    private final AuthenticatedUserService authenticatedUserService;
    private final InvoiceMapper invoiceMapper;

    public CreditCardInvoiceService(CreditCardInvoiceRepository creditCardInvoiceRepository, AuthenticatedUserService authenticatedUserService, InvoiceMapper invoiceMapper) {
        this.creditCardInvoiceRepository = creditCardInvoiceRepository;
        this.authenticatedUserService = authenticatedUserService;
        this.invoiceMapper = invoiceMapper;
    }

    public CreditCardInvoice findOrCreate(CreditCard creditCard, LocalDate purchaseDate) {
        User user = authenticatedUserService.getAuthenticatedUser();

        YearMonth baseMonth = purchaseDate.getDayOfMonth() <= creditCard.getClosingDay()
                ? YearMonth.from(purchaseDate)
                :YearMonth.from(purchaseDate).plusMonths(1);

        LocalDate referenceMonth = baseMonth.atDay(1);

        return creditCardInvoiceRepository.findByCreditCardAndReferenceMonth(creditCard, referenceMonth)
                .orElseGet(() -> {
                    CreditCardInvoice newInvoice = new CreditCardInvoice();
                    newInvoice.setCreditCard(creditCard);
                    newInvoice.setReferenceMonth(referenceMonth);
                    newInvoice.setStatus(InvoiceStatus.OPEN);
                    newInvoice.setUser(user);

                    return creditCardInvoiceRepository.save(newInvoice);
                });

    }

    public List<CreditCardInvoiceDTO> findAll() {
        User user = authenticatedUserService.getAuthenticatedUser();

        List<CreditCardInvoice> invoices = creditCardInvoiceRepository.findAllByUser(user);

        return invoiceMapper.fromEntity(invoices);
    }
}
