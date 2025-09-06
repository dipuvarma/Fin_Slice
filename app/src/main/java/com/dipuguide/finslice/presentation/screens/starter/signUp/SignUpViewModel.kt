package com.dipuguide.finslice.presentation.screens.starter.signUp

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dipuguide.finslice.domain.model.User
import com.dipuguide.finslice.domain.repo.UserAuthRepository
import com.dipuguide.finslice.presentation.common.state.UiState
import com.dipuguide.finslice.utils.getPasswordStrength
import com.dipuguide.finslice.utils.getValidPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: UserAuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _signUpUiState = MutableStateFlow(SignUpUiState())
    val signUpUiState = _signUpUiState.asStateFlow()

    private val _navigation = MutableSharedFlow<SignUpNavigation>()
    val navigation = _navigation.asSharedFlow()


    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.NameChange -> {
                _signUpUiState.update {
                    it.copy(
                        name = event.name,
                        nameError = when {
                            event.name.length < 2 -> "Name must be at least 2 characters"
                            event.name.length > 30 -> "Name must be max 30 characters"
                            else -> null
                        }
                    )
                }
            }

            is SignUpEvent.EmailChange -> {
                val isValid = Patterns.EMAIL_ADDRESS.matcher(event.email).matches()
                _signUpUiState.update {
                    it.copy(
                        email = event.email,
                        emailError = if (!isValid) "Invalid email format" else null
                    )
                }
            }

            is SignUpEvent.PasswordChange -> {
                val strength = getPasswordStrength(event.password)
                val error = getValidPassword(event.password)
                _signUpUiState.update {
                    it.copy(
                        password = event.password,
                        passwordErrors = error,
                        passwordStrength = strength
                    )
                }
            }

            SignUpEvent.PasswordVisibility -> {
                _signUpUiState.update {
                    it.copy(
                        isPasswordVisible = !it.isPasswordVisible
                    )
                }
            }

            SignUpEvent.SignUpClick -> {
                signUp(
                    name = signUpUiState.value.name,
                    email = signUpUiState.value.email,
                    password = signUpUiState.value.password
                )
            }

            SignUpEvent.SignUpWithGoogle -> {
                viewModelScope.launch {
                    _navigation.emit(SignUpNavigation.OnBoard)
                }
            }

            SignUpEvent.SignInClick -> {
                viewModelScope.launch {
                    _navigation.emit(SignUpNavigation.SignIn)
                }
            }
        }
    }

    fun signUp(name: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = authRepository.signUp(name, email, password)
            result.onSuccess {
                val user = User(
                    name = name,
                    email = email
                )
                saveUserDetail(user)
                _uiState.value = UiState.Success("Sign-up successful 🎉")
                _navigation.emit(SignUpNavigation.OnBoard)
                Timber.d("✅ Sign-up success → $email")
            }
            result.onFailure {
                _uiState.value =
                    UiState.Error("Sign-up failed: ${it.localizedMessage ?: "Unknown error"}")
                Timber.e(it, "Sign-up failed")
            }
        }
    }

    fun saveUserDetail(user: User) {
        viewModelScope.launch {
            authRepository.saveUserDetail(user)
        }
    }

    val isFormValid = _signUpUiState.map {
        it.nameError == null &&
                it.emailError == null &&
                it.passwordErrors.isEmpty() &&
                it.password.isNotEmpty()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)


    fun resetPasswordStrengthMessage() {
        _signUpUiState.update {
            it.copy(showPasswordStrength = false)
        }
    }

    fun resetUiEvent() {
        _uiState.value = UiState.Idle
    }

}

