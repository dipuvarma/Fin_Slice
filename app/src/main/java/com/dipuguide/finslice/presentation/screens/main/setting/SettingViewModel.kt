package com.dipuguide.finslice.presentation.screens.main.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dipuguide.finslice.domain.model.User
import com.dipuguide.finslice.domain.repo.ThemeRepository
import com.dipuguide.finslice.domain.repo.UserAuthRepository
import com.dipuguide.finslice.presentation.common.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class SettingViewModel @Inject constructor(
    private val authRepository: UserAuthRepository,
    private val preferences: ThemeRepository,
) : ViewModel() {

    private val _isDarkModeEnabled = MutableStateFlow(false)
    val isDarkModeEnabled: StateFlow<Boolean> = _isDarkModeEnabled.asStateFlow()

    private val _isDynamicModeEnabled = MutableStateFlow(false)
    val isDynamicModeEnabled: StateFlow<Boolean> = _isDynamicModeEnabled.asStateFlow()

    private val _navigation = MutableSharedFlow<SettingNavigation>()
    val navigation = _navigation.asSharedFlow()

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _settingUiState = MutableStateFlow(SettingUiState())
    val settingUiState = _settingUiState.asStateFlow()

    private val _userDetail = MutableStateFlow(User())
    val userDetail = _userDetail.asStateFlow()

    init {
        getUserDetail()
        loadInitialDarkMode()
        loadInitialDynamicMode()
    }

    fun loadInitialDarkMode() {
        viewModelScope.launch {
            preferences.getDarkMode().collectLatest { isDarkMode ->
                _isDarkModeEnabled.value = isDarkMode
            }

        }
    }

    fun loadInitialDynamicMode() {
        viewModelScope.launch {
            preferences.getDynamicMode().collectLatest { isDynamicMode ->
                _isDynamicModeEnabled.value = isDynamicMode
            }
        }
    }

    private fun onDarkModeChange(isDarkMode: Boolean) {
        viewModelScope.launch {
            _isDarkModeEnabled.value = isDarkMode
            preferences.saveDarkMode(isDarkMode)
        }
    }

    private fun onDynamicModeChange(isDynamicMode: Boolean) {
        viewModelScope.launch {
            _isDynamicModeEnabled.value = isDynamicMode
            preferences.saveDynamicMode(isDynamicMode)
        }
    }

    fun onEvent(event: SettingEvent) {
        when (event) {
            is SettingEvent.DarkModeChange -> {
                onDarkModeChange(event.isDarkMode)
            }

            is SettingEvent.DynamicColorChange -> {
                onDynamicModeChange(event.isDynamic)
            }

            is SettingEvent.AppLockChange -> TODO()
            SettingEvent.CheckUpdateClick -> TODO()
            SettingEvent.FeedbackClick -> TODO()
            SettingEvent.PrivacyClick -> TODO()
            SettingEvent.UserGuideClick -> TODO()
            SettingEvent.RateClick -> TODO()
            SettingEvent.SignOutClick -> signOut()

            SettingEvent.DeleteAccountClick -> deleteAccount()
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            val result = authRepository.signOut()
            result.onSuccess {
                _uiState.value = UiState.Success("👋 Signed out")
                _navigation.emit(SettingNavigation.SIGN_OUT)
                Timber.d("👋 User signed out")
            }
            result.onFailure {
                _uiState.value = UiState.Error("👋 Signed out failed!")
                Timber.e(it, "👋 User signed out failed!")
            }
        }
    }

    private fun deleteAccount() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = authRepository.deleteAccount()
            result.onSuccess {
                _uiState.value = UiState.Success("🗑️ Account deleted")
                _navigation.emit(SettingNavigation.DELETE_ACCOUNT)
                Timber.d("🗑️ Account deleted")
            }
            result.onFailure {
                _uiState.value = UiState.Error("🗑️ Account deleted failed!")
                Timber.d("🗑️ Account deleted failed!")
            }
        }
    }

    private fun getUserDetail() {
        viewModelScope.launch {
            authRepository.getUserDetails().collectLatest { result ->
                result.onSuccess { user ->
                    _userDetail.update {
                        it.copy(
                            name = user.name,
                            email = user.email,
                            photoUri = user.photoUri,
                        )
                    }
                }

                result.onFailure { exception ->

                }
            }
        }
    }

    fun resetUiState() {
        _uiState.value = UiState.Idle
    }
}

