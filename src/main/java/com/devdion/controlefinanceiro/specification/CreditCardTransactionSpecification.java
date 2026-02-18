package com.devdion.controlefinanceiro.specification;

import com.devdion.controlefinanceiro.model.CreditCardTransaction;
import com.devdion.controlefinanceiro.model.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class CreditCardTransactionSpecification {

    public static Specification<CreditCardTransaction> withFilters(
            User user,
            Long categoryId,
            String description,
            Long creditCardId,
            LocalDate startDate,
            LocalDate endDate,
            Long invoiceId
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            String search = "%" + description.trim().toLowerCase() + "%";

            predicates.add(criteriaBuilder.equal(root.get("user").get("id"), user.getId()));

            if(invoiceId != null) {
                predicates.add(criteriaBuilder.equal(root.get("invoice").get("id"), invoiceId));
            }
            if(categoryId != null) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("id"), categoryId));
            }

            if(description != null && !description.isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("description")),
                        search
                        ));
            }

            if(creditCardId != null) {
                predicates.add(criteriaBuilder.equal(root.get("invoice").get("creditCard").get("id"), creditCardId));
            }

            if(startDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("purchaseDate"),
                        startDate
                ));
            }

            if(endDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("purchaseDate"),
                        endDate
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
