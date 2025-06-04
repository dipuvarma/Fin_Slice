package com.dipuguide.finslice.data.model

import com.google.firebase.Timestamp


data class IncomeTransaction(
    val id: String? = null,
    val amount: Double = 0.0,
    val note: String? = null,
    val category: String = "",
    val createdAt: Long? = null,
)
