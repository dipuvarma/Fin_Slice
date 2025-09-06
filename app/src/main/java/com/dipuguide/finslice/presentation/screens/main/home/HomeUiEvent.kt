package com.dipuguide.finslice.presentation.screens.main.home

sealed class HomeUiEvent {
    data class MonthPickerClick(val month: Int, val year: Int) : HomeUiEvent()
    object MonthOverviewClick : HomeUiEvent()
}

enum class HomeNavigation() {
    REPORT
}