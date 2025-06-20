package com.dipuguide.finslice.presentation.mapper

import com.dipuguide.finslice.data.model.ExpenseTransaction
import com.dipuguide.finslice.data.model.IncomeTransaction
import com.dipuguide.finslice.presentation.model.ExpenseTransactionUi
import com.dipuguide.finslice.presentation.model.IncomeTransactionUi
import com.dipuguide.finslice.utils.formatPrice

fun IncomeTransaction.toIncomeTransactionUi(): IncomeTransactionUi {
    return IncomeTransactionUi(
        id = id,
        amount = formatPrice(amount),
        note = note ?: "",
        category = category,
        date = createdAt
    )
}

fun IncomeTransactionUi.toIncomeTransaction(): IncomeTransaction {
    return IncomeTransaction(
        id = id,
        amount = amount.toDouble(),
        note = note,
        category = category,
        createdAt = date
    )
}


fun ExpenseTransactionUi.toExpenseTransaction(): ExpenseTransaction {
    return ExpenseTransaction(
        id = id,
        amount = amount.toDouble(),
        note = note,
        category = category,
        tag = tag,
        date = date
    )
}

fun ExpenseTransaction.toExpenseTransactionUi(): ExpenseTransactionUi {
    return ExpenseTransactionUi(
        id = id,
        amount = formatPrice(amount),
        note = note,
        category = category,
        tag = tag,
        date = date
    )
}