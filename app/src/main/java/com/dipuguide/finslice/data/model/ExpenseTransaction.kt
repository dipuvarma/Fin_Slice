package com.dipuguide.finslice.data.model

data class ExpenseTransaction(
    val id: String? = null,
    val amount: Double = 0.0,
    val note: String? = null,
    val category: String = "", // "need", "want", "invest"
    val tag: String? = null,       // e.g., "food", "rent", "entertainment"
    val date: Long? = null
)


