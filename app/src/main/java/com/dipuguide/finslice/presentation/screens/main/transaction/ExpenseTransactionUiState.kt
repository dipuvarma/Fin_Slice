package com.dipuguide.finslice.presentation.screens.main.transaction


data class AllExpenseUiState(
    val selectedTab: Int = 0,
    val totalExpense: String = "",
    val needExpenseAmount: String = "",
    val wantExpenseAmount: String = "",
    val investExpenseAmount: String = "",
    val expenseTransactionList: List<ExpenseTransactionUi> = emptyList(),
)


data class ExpenseTransactionUi(
    val id: String? = null,
    val amount: String = "",
    val note: String? = null,
    val category: String = "", // "need", "want", "invest"
    val tag: String? = null,       // e.g., "food", "rent", "entertainment"
    val date: String? = null,
)
