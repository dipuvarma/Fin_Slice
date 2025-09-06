package com.dipuguide.finslice.presentation.screens.addTransaction.income

sealed class AddIncomeEvent {
    data class AmountChange(val amount: String) : AddIncomeEvent()
    data class NoteChange(val note: String) : AddIncomeEvent()
    data class CategoryChange(val category: String) : AddIncomeEvent()
    data class DatePickerChange(val date: Long) : AddIncomeEvent()
    object SaveClick : AddIncomeEvent()
}

enum class AddIncomeNavigation {
    HOME
}