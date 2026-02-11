package com.devdion.controlefinanceiro.repository;

import com.devdion.controlefinanceiro.dto.dashboard.MonthlyTransactionsDTO;
import com.devdion.controlefinanceiro.dto.dashboard.SumByCategoryDTO;
import com.devdion.controlefinanceiro.model.Transaction;
import com.devdion.controlefinanceiro.model.TransactionType;
import com.devdion.controlefinanceiro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

    Optional<Transaction> findByIdAndUser(Long id, User user);

    List<Transaction> findAllByUserOrderByDateDesc(User user);

    @Query("""
    select coalesce(sum(t.amount), 0)
    from Transaction t
    where t.user = :user
      and t.type = com.devdion.controlefinanceiro.model.TransactionType.INCOME
      and year(t.date) = :year
      and month(t.date) = :month
    """)
    BigDecimal incomeByMonth(
            User user,
            int year,
            int month
    );

    @Query("""
    select coalesce(sum(t.amount), 0)
    from Transaction t
    where t.user = :user
      and t.type = com.devdion.controlefinanceiro.model.TransactionType.EXPENSE
      and year(t.date) = :year
      and month(t.date) = :month
    """)
    BigDecimal expenseByMonth(
            User user,
            int year,
            int month
    );


    @Query("""
    
    select new com.devdion.controlefinanceiro.dto.dashboard.MonthlyTransactionsDTO(
        month(t.date),
        coalesce(sum(case when t.type = 'INCOME' then t.amount else 0 end), 0),
        coalesce(sum(case when t.type = 'EXPENSE' then t.amount else 0 end), 0)
    )
    from Transaction t
    where t.user = :user
      and year(t.date) = :year
    group by month(t.date)
    order by month(t.date)
    
    """)
    List<MonthlyTransactionsDTO> sumByYearGroupedByMonth(User user, int year);

    @Query("""
    select new com.devdion.controlefinanceiro.dto.dashboard.SumByCategoryDTO(
        t.category.id,
        coalesce(sum(t.amount), 0)
    )
    from Transaction t
    where t.user = :user
      and t.type = 'EXPENSE'
      and year(t.date) = :year
    group by t.category.id
""")
    List<SumByCategoryDTO> sumExpenseByCategory(User user, int year);
}
