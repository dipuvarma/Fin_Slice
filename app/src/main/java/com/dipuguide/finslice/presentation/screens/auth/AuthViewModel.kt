package com.dipuguide.finslice.presentation.screens.auth

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dipuguide.finslice.data.repo.FirebaseAuthRepository
import com.dipuguide.finslice.utils.getPasswordStrength
import com.dipuguide.finslice.utils.getValidPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: FirebaseAuthRepository,
) : ViewModel() {

    private val _authUiEvent = MutableStateFlow<AuthUiEvent>(AuthUiEvent.Idle)
    val authUiEvent = _authUiEvent.asStateFlow()

    private val _authUiState = MutableStateFlow(AuthUiState())
    val authUiState = _authUiState.asStateFlow()

    fun signUp(name: String, email: String, password: String) {
        viewModelScope.launch {
            _authUiEvent.value = AuthUiEvent.Loading

            val result = authRepository.signUp(name, email, password)

            _authUiEvent.value = result.fold(
                onSuccess = { AuthUiEvent.Success("Sign-up successful ✅") },
                onFailure = { AuthUiEvent.Error("Sign-up failed ❌") }
            )
        }
    }

    fun signIn(email: String, password: String) {

        viewModelScope.launch {
            _authUiEvent.value = AuthUiEvent.Loading

            val result = authRepository.signIn(email, password)

            _authUiEvent.value = result.fold(
                onSuccess = { AuthUiEvent.Success("Sign-in successful ✅") },
                onFailure = { AuthUiEvent.Error("Sign-in failed ❌") }
            )
        }
    }

    fun forgetPassword(email: String) {
        viewModelScope.launch {
            val result = authRepository.forgetPassword(email)
            _authUiEvent.value = result.fold(
                onSuccess = {
                    AuthUiEvent.Success("Email Sent")
                },
                onFailure = { AuthUiEvent.Error("Password Doesn't Match") }
            )
        }
    }

    fun updateName(name: String) {
        _authUiState.update {
            it.copy(
                user = it.user.copy(name = name),
                nameError = if (name.length < 2) "Name must be at least 2 characters" else if (name.length > 30) "Name must be max 30 characters" else null
            )
        }
    }

    fun updateEmail(email: String) {
        val isValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        _authUiState.update {
            it.copy(
                user = it.user.copy(
                    email = email,
                ),
                emailError = if (!isValid) "Invalid email format" else null
            )
        }
    }

    fun updatePassword(password: String) {
        val errors = getValidPassword(password)
        val strength = getPasswordStrength(password)
        _authUiState.update {
            it.copy(
                user = it.user.copy(password = password),
                passwordErrors = errors,
                passwordStrength = strength,
                showPasswordErrors = true,
                showPasswordStrength = errors.isEmpty()
            )
        }
    }

    fun resetNameEmailOrPass() {
        viewModelScope.launch {
            _authUiState.value = authUiState.value.copy(
                user = AuthUser(
                    name = "",
                    email = "",
                    password = ""
                )
            )
        }
    }

    fun togglePasswordVisibility() {
        _authUiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun isFormValid(): Boolean {
        val state = _authUiState.value
        return state.nameError == null &&
                state.emailError == null &&
                state.passwordErrors.isEmpty() &&
                state.user.password.isNotEmpty()
    }

    fun resetPasswordStrengthMessage() {
        _authUiState.update { it.copy(showPasswordStrength = false) }
    }

    fun clearEvents() {
        _authUiEvent.value = AuthUiEvent.Idle
    }

}


