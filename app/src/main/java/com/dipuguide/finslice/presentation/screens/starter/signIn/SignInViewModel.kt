package com.dipuguide.finslice.presentation.screens.starter.signIn

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
class SignInViewModel @Inject constructor(
    private val authRepository: UserAuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _signInUiState = MutableStateFlow(SignInUiState())
    val signInUiState = _signInUiState.asStateFlow()

    private val _navigation = MutableSharedFlow<SignInNavigation>()
    val navigation = _navigation.asSharedFlow()


    fun onEvent(event: SignInEvent) {
        when (event) {
            is SignInEvent.EmailChange -> {
                val isValid = Patterns.EMAIL_ADDRESS.matcher(event.email).matches()
                _signInUiState.update {
                    it.copy(
                        email = event.email,
                        emailError = if (!isValid) "Invalid email format" else null
                    )
                }
            }

            is SignInEvent.PasswordChange -> {
                val error = getValidPassword(event.password)
                val strength = getPasswordStrength(event.password)
                _signInUiState.update {
                    it.copy(
                        password = event.password,
                        passwordErrors = error,
                        passwordStrength = strength,
                    )
                }
            }

            SignInEvent.ForgetPasswordClick -> {
                viewModelScope.launch {
                    _navigation.emit(SignInNavigation.ForgetPassword)
                }
            }

            SignInEvent.SignInClick -> {
                signIn(
                    email = signInUiState.value.email,
                    password = signInUiState.value.password
                )
            }

            SignInEvent.SigInWithGoogleClick -> {
                viewModelScope.launch {

                    _navigation.emit(SignInNavigation.Main)
                }
            }

            SignInEvent.SignUpClick -> {
                viewModelScope.launch {
                    _navigation.emit(SignInNavigation.SignUp)
                }
            }

            SignInEvent.PasswordVisibility -> {
                _signInUiState.update {
                    it.copy(
                        isPasswordVisible = !it.isPasswordVisible
                    )
                }
            }
        }
    }

    fun saveUserDetail(user: User) {
        viewModelScope.launch {
            authRepository.saveUserDetail(user)
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Idle
            val result = authRepository.signIn(email, password)
            result.onSuccess {
                _uiState.value = UiState.Success("Welcome back! 👋")
                _navigation.emit(SignInNavigation.Main)
                Timber.d("Sign-in success → $email")
            }

            result.onFailure {
                _uiState.value = UiState.Error(
                    "Sign-In Failed : ${it.localizedMessage ?: "Invalid credentials or user not found"}"
                )
                Timber.e(it, "Sign-in failed")
            }
        }
    }


    fun resetUiEvent() {
        _uiState.value = UiState.Idle
    }


}