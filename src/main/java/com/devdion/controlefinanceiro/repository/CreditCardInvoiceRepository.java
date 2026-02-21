package com.devdion.controlefinanceiro.repository;

import com.devdion.controlefinanceiro.dto.dashboard.CategorySummaryDTO;
import com.devdion.controlefinanceiro.model.CreditCard;
import com.devdion.controlefinanceiro.model.CreditCardInvoice;
import com.devdion.controlefinanceiro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CreditCardInvoiceRepository extends JpaRepository<CreditCardInvoice, Long> {

    Optional<CreditCardInvoice> findByCreditCardAndReferenceMonth(CreditCard creditCard, LocalDate referenceMonth);

    List<CreditCardInvoice> findAllByUser(User user);

    @Query("""
       SELECT new com.devdion.controlefinanceiro.dto.dashboard.CategorySummaryDTO(
           t.category.name,
           SUM(t.installmentAmount)
       )
       FROM CreditCardTransaction t
       WHERE t.invoice.user = :user
       AND EXTRACT(YEAR FROM t.invoice.referenceMonth) = :year
       AND EXTRACT(MONTH FROM t.invoice.referenceMonth) = :month
       GROUP BY t.category.name
       ORDER BY SUM(t.installmentAmount) DESC
       """)
    List<CategorySummaryDTO> sumCurrentMonthByCategory(
            @Param("user") User user,
            @Param("year") int year,
            @Param("month") int month
    );
}
