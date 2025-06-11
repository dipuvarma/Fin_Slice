package com.dipuguide.finslice.presentation.screens.main.history

import com.dipuguide.finslice.presentation.screens.main.transaction.ExpenseTransactionUiEvent

sealed class IncomeHistoryUiEvent {
    object Idle : IncomeHistoryUiEvent()
    object Loading : IncomeHistoryUiEvent()
    data class Success(val message: String) : IncomeHistoryUiEvent()
    data class Error(val message: String) : IncomeHistoryUiEvent()
}