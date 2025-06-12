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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val dataStoreRepo: DataStoreRepository,
) : ViewModel() {

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkModeState: StateFlow<Boolean> = _isDarkMode.asStateFlow()


    init {
        loadDarkMode()
    }

    private fun loadDarkMode() {
        viewModelScope.launch {
            dataStoreRepo.isDarkMode().collect {
                _isDarkMode.value = it
            }

        }
    }

    fun toggleDarkMode(enabled: Boolean) {
        Log.d("darkmode", "toggleDarkMode: $enabled")
        viewModelScope.launch {
            if (enabled) {
                dataStoreRepo.darkModeOn()
            } else {
                dataStoreRepo.darkModeOff()
            }
            Log.d("darkmode", "isDarkMode: ${dataStoreRepo.isDarkMode()}")
            dataStoreRepo.isDarkMode().collectLatest {
                _isDarkMode.value = it
            }

        }
    }
}
