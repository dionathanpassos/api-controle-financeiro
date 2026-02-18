package com.devdion.controlefinanceiro.service;

import com.devdion.controlefinanceiro.model.CreditCard;
import com.devdion.controlefinanceiro.model.CreditCardInvoice;
import com.devdion.controlefinanceiro.model.InvoiceStatus;
import com.devdion.controlefinanceiro.repository.CreditCardInvoiceRepository;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Service
public class CreditCardInvoiceService {

    private final CreditCardInvoiceRepository creditCardInvoiceRepository;


    public CreditCardInvoiceService(CreditCardInvoiceRepository creditCardInvoiceRepository) {
        this.creditCardInvoiceRepository = creditCardInvoiceRepository;
    }

    public CreditCardInvoice findOrCreate(CreditCard creditCard, LocalDate purchaseDate) {

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

                    return creditCardInvoiceRepository.save(newInvoice);
                });

    }
}
