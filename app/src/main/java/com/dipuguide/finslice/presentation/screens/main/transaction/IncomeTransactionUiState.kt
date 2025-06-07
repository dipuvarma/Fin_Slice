package com.dipuguide.finslice.presentation.screens.main.transaction


data class IncomeTransactionUiState(
    val selectedTab: Int = 0,
    val totalIncome: String = "",
    val averageIncome: String = "",
    val needPercentageAmount: String = "",
    val wantPercentageAmount: String = "",
    val investPercentageAmount: String = "",
    val incomeTransactionList: List<IncomeTransactionUi> = emptyList(),
)

data class IncomeTransactionUi(
    val id: String? = null,
    val amount: String = "",
    val note: String? = null,
    val category: String = "",
    val date: String? = null,
)
