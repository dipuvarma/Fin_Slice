package com.dipuguide.finslice.presentation.screens.main.home



data class HomeTransactionUiState(
    val totalIncome: String = "",
    val totalExpense: String = "",
    val netBalance: String = "",
    val needTotal: String = "",
    val wantTotal: String = "",
    val investTotal: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
)