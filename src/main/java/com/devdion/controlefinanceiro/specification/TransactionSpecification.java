package com.devdion.controlefinanceiro.specification;

import com.devdion.controlefinanceiro.model.Transaction;
import com.devdion.controlefinanceiro.model.TransactionType;
import com.devdion.controlefinanceiro.model.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TransactionSpecification {

    public static Specification<Transaction> withFilters(
            TransactionType type,
            User user
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(
                    criteriaBuilder.equal(root.get("user").get("id"), user.getId())
            );

            if (type != null) {
                predicates.add(criteriaBuilder.equal(root.get("type"), type));
            }



            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
