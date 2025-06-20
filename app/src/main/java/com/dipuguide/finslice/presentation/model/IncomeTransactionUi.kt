package com.dipuguide.finslice.presentation.model

data class IncomeTransactionUi(
    val id: String? = null,
    val amount: String = "",
    val note: String? = null,
    val category: String = "",
    val date: Long? = null,
)
