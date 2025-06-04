package com.dipuguide.finslice.presentation.screens.main

import com.dipuguide.finslice.utils.Category
import com.dipuguide.finslice.utils.Type

data class TransactionUiState(
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val netBalance: Double = 0.0,
    val needTotal: Double = 0.0,
    val wantTotal: Double = 0.0,
    val investTotal: Double = 0.0,
    val isLoading: Boolean = false,
    val error: String? = null,
)

data class IncomeTransactionUiState(
    val incomeTransactionList: List<IncomeTransactionUi> = emptyList(),
)

data class IncomeTransactionUi(
    val id: String? = null,
    val amount: String = "",
    val note : String? = null,
    val category: String = "",
    val date: String? = null,
)
