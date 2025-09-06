package com.dipuguide.finslice.presentation.screens.starter.forgetPassword

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dipuguide.finslice.domain.repo.UserAuthRepository
import com.dipuguide.finslice.presentation.common.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ForgetViewModel @Inject constructor(
    private val authRepository: UserAuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _forgetUiState = MutableStateFlow(ForgetUiState())
    val forgetUiState = _forgetUiState.asStateFlow()

    private val _navigation = MutableSharedFlow<ForgetNavigation>()
    val navigation = _navigation.asSharedFlow()


    fun onEvent(event: ForgetEvent) {
        when (event) {
            is ForgetEvent.EmailChange -> {
                val isValid = Patterns.EMAIL_ADDRESS.matcher(event.email).matches()
                _forgetUiState.update {
                    it.copy(
                        email = event.email,
                        emailError = if (isValid) "Please enter a valid email" else null
                    )
                }
            }

            ForgetEvent.SendRestClick -> {
                forgetPassword(
                    email = forgetUiState.value.email
                )
            }

            ForgetEvent.SignInClick -> {
                viewModelScope.launch {
                    _navigation.emit(ForgetNavigation.SignIn)
                }
            }
        }
    }


    fun forgetPassword(email: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = authRepository.forgetPassword(email)
            result.onSuccess {
                _uiState.value = UiState.Success("Password reset link sent to $email 📧")
                _navigation.emit(ForgetNavigation.SignIn)
                Timber.d("📤 Sent reset link to $email")
            }

            result.onFailure {
                _uiState.value = UiState.Error(
                    it.localizedMessage ?: "Unable to send reset link. Please check the email."
                )
                Timber.e(it, "❌ Failed to send password reset link")
            }
        }
    }

    fun resetUiState() {
        _uiState.value = UiState.Idle
    }


}