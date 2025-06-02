package com.dipuguide.finslice.presentation.screens.auth

sealed class AuthUiEvent {
    object Idle : AuthUiEvent()
    object Loading : AuthUiEvent()
    data class Success(val message: String) : AuthUiEvent()
    data class Error(val message: String) : AuthUiEvent()
}
