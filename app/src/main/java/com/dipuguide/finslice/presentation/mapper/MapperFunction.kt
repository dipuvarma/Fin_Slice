package com.dipuguide.finslice.presentation.mapper

import com.dipuguide.finslice.data.model.IncomeTransaction
import com.dipuguide.finslice.presentation.screens.main.transaction.IncomeTransactionUi
import com.dipuguide.finslice.utils.formatPrice
import com.dipuguide.finslice.utils.formatTimestampToDateTime


fun IncomeTransaction.toIncomeTransactionUi(): IncomeTransactionUi {
    return IncomeTransactionUi(
        id = id,
        amount = formatPrice(amount),
        note = note ?: "",
        category = category,
        date = formatTimestampToDateTime(createdAt!!)
    )
}

fun IncomeTransactionUi.toIncomeTransaction(): IncomeTransaction{
    return IncomeTransaction(
        id = id,
        amount = amount.toDouble(),
        note = note,
        category = category,
        createdAt = System.currentTimeMillis()
    )
}