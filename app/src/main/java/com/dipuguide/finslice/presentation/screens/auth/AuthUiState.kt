package com.dipuguide.finslice.presentation.screens.auth

import com.dipuguide.finslice.utils.PasswordStrength

data class AuthUiState(
    val user: AuthUser = AuthUser(),
    val isPasswordVisible: Boolean = false,
    val nameError: String? = null,
    val emailError: String? = null,
    val passwordErrors: List<String> = emptyList(),
    val showPasswordErrors: Boolean = false,
    val showPasswordStrength: Boolean = false,
    val passwordStrength: PasswordStrength = PasswordStrength.WEAK,
)


data class AuthUser(
    val name: String = "",
    val email: String = "",
    val password: String = "",
)