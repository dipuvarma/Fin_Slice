package com.dipuguide.finslice.presentation.screens.main

import com.dipuguide.finslice.presentation.screens.auth.AuthUiEvent

sealed class IncomeUiEvent {
    object Idle : IncomeUiEvent()
    object Loading : IncomeUiEvent()
    data class Success(val message: String) : IncomeUiEvent()
    data class Error(val message: String) : IncomeUiEvent()
}