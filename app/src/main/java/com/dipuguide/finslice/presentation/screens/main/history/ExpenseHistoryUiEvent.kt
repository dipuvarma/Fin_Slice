package com.dipuguide.finslice.presentation.screens.main.history

sealed class ExpenseHistoryUiEvent {
    object Idle : ExpenseHistoryUiEvent()
    object Loading : ExpenseHistoryUiEvent()
    data class Success(val message: String) : ExpenseHistoryUiEvent()
    data class Error(val message: String) : ExpenseHistoryUiEvent()
}