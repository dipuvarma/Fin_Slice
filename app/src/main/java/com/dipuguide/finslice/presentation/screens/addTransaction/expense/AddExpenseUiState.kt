package com.dipuguide.finslice.presentation.screens.addTransaction.expense

data class AddExpenseUiState(
    val amount: String = "",
    val note: String = "",
    val category: String = "",
    val tag: String = "",
    val createdAt: Long = System.currentTimeMillis(),
)
