package com.dipuguide.finslice.presentation.model


data class ExpenseTransactionUi(
    val id: String? = null,
    val amount: String = "",
    val note: String? = null,
    val category: String = "", // "need", "want", "invest"
    val tag: String? = null,       // e.g., "food", "rent", "entertainment"
    val date: Long? = null,
)
