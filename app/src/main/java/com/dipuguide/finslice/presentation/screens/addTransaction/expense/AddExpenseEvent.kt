package com.dipuguide.finslice.presentation.screens.addTransaction.expense

sealed class AddExpenseEvent {
    data class AmountChange(val amount: String) : AddExpenseEvent()
    data class NoteChange(val note: String) : AddExpenseEvent()
    data class CategoryChange(val category: String) : AddExpenseEvent()
    data class TagChange(val tag: String) : AddExpenseEvent()
    object SaveExpenseClick : AddExpenseEvent()
    data class DatePickerClick(val millis: Long) : AddExpenseEvent()
}

enum class AddExpenseNavigationEvent {
    MAIN
}