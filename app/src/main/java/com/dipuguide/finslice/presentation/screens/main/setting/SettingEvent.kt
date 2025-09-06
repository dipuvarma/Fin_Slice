package com.dipuguide.finslice.presentation.screens.main.setting

sealed class SettingEvent {
    data class DarkModeChange(val isDarkMode: Boolean) : SettingEvent()
    data class DynamicColorChange(val isDynamic: Boolean) : SettingEvent()
    data class AppLockChange(val isAppLock: Boolean) : SettingEvent()
    object CheckUpdateClick : SettingEvent()
    object RateClick : SettingEvent()
    object PrivacyClick : SettingEvent()
    object UserGuideClick : SettingEvent()
    object FeedbackClick : SettingEvent()
    object SignOutClick : SettingEvent()
    object DeleteAccountClick : SettingEvent()
}

enum class SettingNavigation() {
    RATE, PRIVACY, FEEDBACK, SIGN_OUT, DELETE_ACCOUNT
}