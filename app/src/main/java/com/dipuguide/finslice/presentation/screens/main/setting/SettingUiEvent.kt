package com.dipuguide.finslice.presentation.screens.main.setting

import com.dipuguide.finslice.presentation.screens.main.home.HomeUiEvent

sealed class SettingUiEvent {
    object Idle : SettingUiEvent()
    object Loading : SettingUiEvent()
    data class Success(val message: String) : SettingUiEvent()
    data class Error(val message: String) : SettingUiEvent()
}