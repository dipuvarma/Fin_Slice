package com.dipuguide.finslice.presentation.screens.main.category

import com.dipuguide.finslice.domain.model.ExpenseTransaction

data class CategoryUiState(
    val expenseNeedList : List<ExpenseTransaction> = emptyList(),
    val expenseWantList : List<ExpenseTransaction> = emptyList(),
    val expenseInvestList : List<ExpenseTransaction> = emptyList()
)
