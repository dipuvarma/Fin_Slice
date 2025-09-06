package com.dipuguide.finslice.presentation.screens.addTransaction.income

data class AddIncomeUiState(
    val amount: String = "",
    val note: String? = null,
    val category: String = "",
    val createdAt: Long = System.currentTimeMillis(),
)