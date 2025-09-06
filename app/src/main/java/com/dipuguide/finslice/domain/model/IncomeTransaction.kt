package com.dipuguide.finslice.domain.model

data class IncomeTransaction(
    val id: String = "",
    val amount: Double = 0.0,
    val note: String? = null,
    val category: String = "",
    val createdAt: Long = System.currentTimeMillis(),
)
