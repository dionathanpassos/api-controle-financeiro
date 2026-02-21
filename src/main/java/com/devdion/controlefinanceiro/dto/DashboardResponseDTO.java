package com.devdion.controlefinanceiro.dto;

import com.devdion.controlefinanceiro.dto.account.AccountBalanceDTO;
import com.devdion.controlefinanceiro.dto.dashboard.*;
import com.devdion.controlefinanceiro.model.Category;

import java.math.BigDecimal;
import java.util.List;

public record DashboardResponseDTO(
        List<AccountBalanceDTO> balanceByAccount,
        BigDecimal balance,
        BigDecimal income,
        BigDecimal expense,
        List<MonthlyTransactionsDTO> totalByMonth,
        List<CategoryTotalDTO> expenseByCategory,
        List<CardExpenseDTO> cardExpense,
        List<CategorySummaryDTO> cardBycategory

) {
}
