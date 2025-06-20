package com.dipuguide.finslice.presentation.screens.auth

import android.net.Uri
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.dipuguide.finslice.data.repo.DataStoreRepository
import com.dipuguide.finslice.data.repo.FirebaseAuthRepository
import com.dipuguide.finslice.utils.Destination
import com.dipuguide.finslice.utils.PasswordStrength
import com.dipuguide.finslice.utils.getPasswordStrength
import com.dipuguide.finslice.utils.getValidPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class GetUserDetail(
    val name: String? = null,
    val email: String? = null,
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: FirebaseAuthRepository,
    private val dataStoreRepo: DataStoreRepository,
) : ViewModel() {

    // UI State: exposed to UI to drive recomposition
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    // UI Events: one-time signals for navigation, messages, etc.
    private val _uiEvent = MutableSharedFlow<AuthUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    // Navigation events
    private val _navigation = MutableSharedFlow<Destination>()
    val navigation = _navigation.asSharedFlow()

    // Login State
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    private val _getUserDetail = MutableStateFlow(GetUserDetail())
    val getUserDetails = _getUserDetail.asStateFlow()

    init {
        checkLoggedInStatus()
        getUserDetails()
    }

    fun saveUserDetails(name: String?, email: String?, photo: Uri?, phoneNumber: String?) {
        viewModelScope.launch {
            dataStoreRepo.saveName(name)
            dataStoreRepo.saveEmail(email)
            dataStoreRepo.savePhoto(photo)
            dataStoreRepo.savePhoneNumber(phoneNumber)
            Log.d("AuthViewModel", "saveUserDetails: $name $email $photo $phoneNumber")
        }
    }

    fun getUserDetails() {
        viewModelScope.launch {
            val user = authRepository.getCurrentUser()
            val name = user?.email?.take(5) ?: ""
            _getUserDetail.update {
                it.copy(
                    name = name,
                    email = user?.email
                )
            }
        }
    }

    fun checkLoggedInStatus() {
        viewModelScope.launch {
            val loggedIn = dataStoreRepo.isLoggedIn()
            _isLoggedIn.value = loggedIn
            _navigation.emit(
                if (loggedIn) Destination.Main else Destination.GettingStart
            )
        }
    }


    fun onLoggedIn() {
        viewModelScope.launch {
            dataStoreRepo.onLoggedIn()
            _isLoggedIn.value = true
        }
    }


    fun signUp(name: String, email: String, password: String) {
        viewModelScope.launch {
            _uiEvent.emit(AuthUiEvent.Loading)

            val result = authRepository.signUp(name, email, password)

            result.fold(
                onSuccess = {
                    dataStoreRepo.onLoggedIn()
                    _isLoggedIn.value = true
                    _uiEvent.emit(AuthUiEvent.Success("Sign-up successful ✅"))
                    _navigation.emit(Destination.Main)
                },
                onFailure = {
                    _uiEvent.emit(AuthUiEvent.Error("Sign-up failed ❌"))
                }
            )
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _uiEvent.emit(AuthUiEvent.Loading)

            val result = authRepository.signIn(email, password)

            result.fold(
                onSuccess = {
                    dataStoreRepo.onLoggedIn()
                    _isLoggedIn.value = true
                    _uiEvent.emit(AuthUiEvent.Success("Sign-in successful ✅"))
                    _navigation.emit(Destination.Main)
                },
                onFailure = {
                    _uiEvent.emit(AuthUiEvent.Error("Sign-in failed ❌"))
                }
            )
        }
    }

    fun forgetPassword(email: String) {
        viewModelScope.launch {
            val result = authRepository.forgetPassword(email)
            result.fold(
                onSuccess = {
                    _uiEvent.emit(AuthUiEvent.Success("Email sent to reset password 📧"))
                },
                onFailure = {
                    _uiEvent.emit(AuthUiEvent.Error("Failed to send reset link ❌"))
                }
            )
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
            dataStoreRepo.signOut()
            _navigation.emit(Destination.GettingStart)
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            authRepository.deleteAccount()
            _navigation.emit(Destination.GettingStart)
        }
    }

    fun updateName(name: String) {
        _uiState.update {
            it.copy(
                user = it.user.copy(name = name),
                nameError = when {
                    name.length < 2 -> "Name must be at least 2 characters"
                    name.length > 30 -> "Name must be max 30 characters"
                    else -> null
                }
            )
        }
    }

    fun updateEmail(email: String) {
        val isValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        _uiState.update {
            it.copy(
                user = it.user.copy(email = email),
                emailError = if (!isValid) "Invalid email format" else null
            )
        }
    }

    fun updatePassword(password: String) {
        val errors = getValidPassword(password)
        val strength = getPasswordStrength(password)
        _uiState.update {
            it.copy(
                user = it.user.copy(password = password),
                passwordErrors = errors,
                passwordStrength = strength,
                showPasswordErrors = true,
                showPasswordStrength = errors.isEmpty()
            )
        }
    }

    fun resetForm() {
        _uiState.update {
            it.copy(
                user = AuthUserUi(),
                nameError = null,
                emailError = null,
                passwordErrors = emptyList(),
                passwordStrength = PasswordStrength.WEAK,
                showPasswordErrors = false,
                showPasswordStrength = false
            )
        }
    }

    fun togglePasswordVisibility() {
        _uiState.update {
            it.copy(isPasswordVisible = !it.isPasswordVisible)
        }
    }


    val isFormValid = uiState.map {
        it.nameError == null &&
                it.emailError == null &&
                it.passwordErrors.isEmpty() &&
                it.user.password.isNotEmpty()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)


    fun resetPasswordStrengthMessage() {
        _uiState.update {
            it.copy(showPasswordStrength = false)
        }
    }

}





