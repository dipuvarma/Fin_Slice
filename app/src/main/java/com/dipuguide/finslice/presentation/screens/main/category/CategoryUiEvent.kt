package com.dipuguide.finslice.presentation.screens.main.category

import com.dipuguide.finslice.presentation.screens.main.home.HomeUiEvent

sealed class CategoryUiEvent {
    object Idle : CategoryUiEvent()
    object Loading : CategoryUiEvent()
    data class Success(val message: String) : CategoryUiEvent()
    data class Error(val message: String) : CategoryUiEvent()
}