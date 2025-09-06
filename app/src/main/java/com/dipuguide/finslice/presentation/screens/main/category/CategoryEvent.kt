package com.dipuguide.finslice.presentation.screens.main.category

import com.dipuguide.finslice.domain.model.ExpenseTransaction

sealed class CategoryEvent {
    data class DeleteClick(val expenseId: String) : CategoryEvent()
    data class EditClick(val expenseTransaction: ExpenseTransaction) : CategoryEvent()
}