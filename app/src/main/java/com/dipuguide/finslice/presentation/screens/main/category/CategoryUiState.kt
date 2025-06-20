package com.dipuguide.finslice.presentation.screens.main.category

import com.dipuguide.finslice.presentation.model.ExpenseTransactionUi

data class CategoryUiState(
   val expenseNeedList : List<ExpenseTransactionUi> = emptyList(),
   val expenseWantList : List<ExpenseTransactionUi> = emptyList(),
   val expenseInvestList : List<ExpenseTransactionUi> = emptyList()
)
