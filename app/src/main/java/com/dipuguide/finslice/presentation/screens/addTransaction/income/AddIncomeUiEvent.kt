package com.dipuguide.finslice.presentation.screens.addTransaction.income

sealed class AddIncomeUiEvent {
    object Idle : AddIncomeUiEvent()
    object Loading : AddIncomeUiEvent()
    data class Success(val message: String) : AddIncomeUiEvent()
    data class Error(val message: String) : AddIncomeUiEvent()
}