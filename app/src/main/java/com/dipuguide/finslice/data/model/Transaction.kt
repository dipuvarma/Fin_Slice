package com.dipuguide.finslice.data.model

import com.dipuguide.finslice.utils.Category
import com.dipuguide.finslice.utils.Type
import com.google.firebase.Timestamp

data class Transaction(
    val id: String? = null,
    val type: Type = Type.Income, // "income" or "expense"
    val amount: String = "",
    val note: String = "",
    val date: Long? = null,
    val category: Category = Category.Need, // "need", "want", "invest"
    val tag: String? = null,       // e.g., "food", "rent", "entertainment"
)


