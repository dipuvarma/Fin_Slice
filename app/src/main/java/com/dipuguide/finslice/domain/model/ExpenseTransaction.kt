package com.dipuguide.finslice.domain.model


data class ExpenseTransaction(
    val id: String = "",
    val amount: Double = 0.0,
    val note: String? = null,
    val category: String = "",
    val tag: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
)
