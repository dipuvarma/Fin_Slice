package com.dipuguide.finslice.presentation.screens.starter.signIn

import com.dipuguide.finslice.utils.PasswordStrength

data class SignInUiState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordErrors: List<String> = emptyList(),
    val isPasswordVisible: Boolean = false,
    val showPasswordErrors: Boolean = false,
    val showPasswordStrength: Boolean = false,
    val passwordStrength: PasswordStrength = PasswordStrength.WEAK,
)
