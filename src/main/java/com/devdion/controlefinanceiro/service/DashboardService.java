package com.devdion.controlefinanceiro.service;

import com.devdion.controlefinanceiro.dto.DashboardResponseDTO;
import com.devdion.controlefinanceiro.dto.account.AccountBalanceDTO;
import com.devdion.controlefinanceiro.dto.dashboard.CategoryTotalDTO;
import com.devdion.controlefinanceiro.dto.dashboard.MonthlyTransactionsDTO;
import com.devdion.controlefinanceiro.dto.dashboard.SubcategoryTotalDTO;
import com.devdion.controlefinanceiro.dto.dashboard.SumByCategoryDTO;
import com.devdion.controlefinanceiro.exception.ResourceNotFoundException;
import com.devdion.controlefinanceiro.model.Category;
import com.devdion.controlefinanceiro.model.TransactionStatus;
import com.devdion.controlefinanceiro.model.TransactionType;
import com.devdion.controlefinanceiro.model.User;
import com.devdion.controlefinanceiro.repository.AccountRepository;
import com.devdion.controlefinanceiro.repository.CategoryRepository;
import com.devdion.controlefinanceiro.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final AccountRepository accountRepository;
    private final UserContextService userContextService;


    public DashboardService(TransactionRepository transactionRepository, CategoryRepository categoryRepositoy, CategoryRepository categoryRepository, AccountRepository accountRepository, UserContextService userContextService) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
        this.accountRepository = accountRepository;
        this.userContextService = userContextService;
    }

    public DashboardResponseDTO getDashboard() {
        User user = userContextService.getCurrentUser();

        LocalDateTime now = LocalDateTime.now();
        int monthNow = now.getMonthValue();
        int yearNow = now.getYear();

        List<AccountBalanceDTO> balanceByAccount = accountRepository.balanceByAccount(user);

        BigDecimal balance = accountRepository.balanceTotal(user, TransactionStatus.ACTIVE);
        BigDecimal income = transactionRepository.incomeByMonth(user, yearNow, monthNow);
        BigDecimal expense = transactionRepository.expenseByMonth(user, yearNow, monthNow);

        List<MonthlyTransactionsDTO> totalByMonth = transactionRepository.sumByYearGroupedByMonth(user, yearNow);

        List<CategoryTotalDTO> expenseByCategory = expenseByCategory(user, yearNow);



        return new DashboardResponseDTO(
                balanceByAccount,
                balance,
                income,
                expense,
                totalByMonth,
                expenseByCategory


        );
    }

    public List<CategoryTotalDTO> expenseByCategory(User user, int year) {

        List<Category> categories = categoryRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Categoria "));

        List<SumByCategoryDTO> sums =
                transactionRepository.sumExpenseByCategory(user, year);

        Map<Long, BigDecimal> totalsMap = sums.stream()
                .collect(Collectors.toMap(
                        SumByCategoryDTO::categoryId,
                        SumByCategoryDTO::total
                ));

        return categories.stream()
                .filter(c -> c.getParent() == null)
                .map(parent -> {

                    List<SubcategoryTotalDTO> subs = parent.getSubcategories()
                            .stream()
                            .map(sub -> new SubcategoryTotalDTO(
                                    sub.getId(),
                                    sub.getName(),
                                    totalsMap.getOrDefault(
                                            sub.getId(),
                                            BigDecimal.ZERO)
                            ))
                            // ❗ opcional: remove subcategoria sem gasto
                            .filter(sub -> sub.total().compareTo(BigDecimal.ZERO) > 0)
                            .toList();

                    BigDecimal parentTotal = subs.stream()
                            .map(SubcategoryTotalDTO::total)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    // ❗ opcional: remove categoria pai sem gasto
                    if (parentTotal.compareTo(BigDecimal.ZERO) == 0) {
                        return null;
                    }

                    return new CategoryTotalDTO(
                            parent.getId(),
                            parent.getName(),
                            parentTotal,
                            subs
                    );
                })
                .filter(Objects::nonNull)
                .toList();
    }


}
