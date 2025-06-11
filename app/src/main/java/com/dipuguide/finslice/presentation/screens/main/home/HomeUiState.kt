package com.dipuguide.finslice.presentation.screens.main.home



data class HomeUiState(
    val averageIncome: String = "",
    val netBalance: String = "",
    val totalIncome: String = "",
    val totalExpense: String = "",
    val needExpenseTotal: String = "",
    val wantExpenseTotal: String = "",
    val investExpenseTotal: String = "",
    val needPercentageAmount: String = "",
    val wantPercentageAmount: String = "",
    val investPercentageAmount: String = "",
)