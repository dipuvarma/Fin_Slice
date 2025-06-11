package com.dipuguide.finslice.presentation.screens.main.home

import com.dipuguide.finslice.presentation.screens.main.transaction.IncomeUiEvent

sealed class HomeUiEvent {
    object Idle : HomeUiEvent()
    object Loading : HomeUiEvent()
    data class Success(val message: String) : HomeUiEvent()
    data class Error(val message: String) : HomeUiEvent()
}