package com.dipuguide.finslice.data.model

data class ExpenseTransaction(
    val id: String? = null,
    val amount: Double = 0.0,
    val note: String? = null,
    val category: String = "",
    val tag: String? = null,
    val date: Long? = null
)


