package com.dipuguide.finslice.presentation.screens.main.setting

import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dipuguide.finslice.R
import com.dipuguide.finslice.data.repo.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val dataStoreRepo: DataStoreRepository,
) : ViewModel() {

    // Internal mutable state
    private val _isDarkMode = MutableStateFlow(false)
    private val _isDynamicMode = MutableStateFlow(false)

    // Public immutable state
    val isDarkModeState: StateFlow<Boolean> = _isDarkMode.asStateFlow()
    val isDynamicModeState: StateFlow<Boolean> = _isDynamicMode.asStateFlow()

    init {
        observeSettings()
    }

    /**
     * 💬 Observes the current dark mode and dynamic mode settings from DataStore.
     * 🔥 FIX: Use separate coroutines to avoid collect() blocking each other.
     */
    private fun observeSettings() {
        viewModelScope.launch {
            dataStoreRepo.isDarkMode()
                .catch { ex -> Log.e("SettingsVM", "Failed to load dark mode", ex) }
                .collect { isDark ->
                    Log.d("SettingsVM", "Dark mode state updated: $isDark")
                    _isDarkMode.value = isDark
                }
        }

        viewModelScope.launch {
            dataStoreRepo.isDynamicMode()
                .catch { ex -> Log.e("SettingsVM", "Failed to load dynamic mode", ex) }
                .collect { isDynamic ->
                    Log.d("SettingsVM", "Dynamic mode state updated: $isDynamic")
                    _isDynamicMode.value = isDynamic
                }
        }
    }

    /**
     * 💡 Toggle dark mode preference
     */
    fun toggleDarkMode(enabled: Boolean) {
        Log.d("SettingsVM", "Toggling dark mode to: $enabled")
        viewModelScope.launch {
            runCatching {
                if (enabled) dataStoreRepo.darkModeOn() else dataStoreRepo.darkModeOff()
            }.onFailure {
                Log.e("SettingsVM", "Failed to toggle dark mode", it)
            }
        }
    }

    /**
     * 💡 Toggle dynamic color mode preference
     */
    fun toggleDynamicMode(enabled: Boolean) {
        Log.d("SettingsVM", "Toggling dynamic mode to: $enabled")
        viewModelScope.launch {
            runCatching {
                if (enabled) dataStoreRepo.dynamicModeOn() else dataStoreRepo.dynamicModeOff()
            }.onFailure {
                Log.e("SettingsVM", "Failed to toggle dynamic mode", it)
            }
        }
    }
}

