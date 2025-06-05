package com.dipuguide.finslice.presentation.screens.main.transaction

sealed class ExpenseTransactionUiEvent {
    object Idle : ExpenseTransactionUiEvent()
    object Loading : ExpenseTransactionUiEvent()
    data class Success(val message: String) : ExpenseTransactionUiEvent()
    data class Error(val message: String) : ExpenseTransactionUiEvent()
}