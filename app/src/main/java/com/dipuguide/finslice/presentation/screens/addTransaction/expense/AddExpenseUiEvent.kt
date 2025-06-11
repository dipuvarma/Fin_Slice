package com.dipuguide.finslice.presentation.screens.addTransaction.expense

sealed class AddExpenseUiEvent {
    object Idle : AddExpenseUiEvent()
    object Loading : AddExpenseUiEvent()
    data class Success(val message: String) : AddExpenseUiEvent()
    data class Error(val message: String) : AddExpenseUiEvent()
}