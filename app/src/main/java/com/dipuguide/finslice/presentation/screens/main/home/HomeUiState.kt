package com.dipuguide.finslice.presentation.screens.main.home

import android.os.Build
import java.time.LocalDate
import java.util.Calendar

data class HomeUiState(
    val averageIncome: String = "",
    val netAmount: String = "",
    val totalIncome: String = "",
    val totalExpense: String = "",
    val needExpenseTotal: String = "",
    val wantExpenseTotal: String = "",
    val investExpenseTotal: String = "",
    val needPercentageAmount: String = "",
    val wantPercentageAmount: String = "",
    val investPercentageAmount: String = "",
    val selectedMonth: Int = currentMonth,
    val selectedYear: Int = currentYear,
)



private val currentMonth = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    LocalDate.now().monthValue
} else {
    Calendar.getInstance().get(Calendar.MONTH) + 1
}

private val currentYear = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    LocalDate.now().year
} else {
    Calendar.getInstance().get(Calendar.YEAR)
}