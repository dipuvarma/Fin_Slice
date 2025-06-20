package com.dipuguide.finslice.presentation.screens.main.history

sealed class IncomeHistoryUiEvent {
    object Idle : IncomeHistoryUiEvent()
    object Loading : IncomeHistoryUiEvent()
    data class Success(val message: String) : IncomeHistoryUiEvent()
    data class Error(val message: String) : IncomeHistoryUiEvent()
}