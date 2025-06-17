package com.dipuguide.finslice.presentation.screens.main.report

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dipuguide.finslice.data.repo.ExpenseTransactionRepo
import com.dipuguide.finslice.presentation.screens.main.transaction.ExpenseTransactionUi
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.ehsannarmani.compose_charts.models.Pie
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val expenseTransactionRepo: ExpenseTransactionRepo,
) : ViewModel() {


    private val _allExpenseByMonth = MutableStateFlow<List<ExpenseTransactionUi>>(emptyList())
    val allExpenseByMonth = _allExpenseByMonth.asStateFlow()

    private val _reportUiEvent = MutableSharedFlow<ReportUiEvent>()
    val reportUiEvent = _reportUiEvent.asSharedFlow()

    private val _reportUiState = MutableStateFlow(ReportUiState())
    val reportUiState = _reportUiState.asStateFlow()


    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getAllExpenseByMonth(LocalDate.now().monthValue, LocalDate.now().year)
        }
    }

    fun onMonthYearSelected(month: Int, year: Int) {
        getAllExpenseByMonth(month, year)
        _reportUiState.update {
            it.copy(
                selectedMonth = month,
                selectedYear = year
            )
        }

    }

    private fun getAllExpenseByMonth(month: Int, year: Int) {
        viewModelScope.launch {
            _reportUiEvent.emit(ReportUiEvent.Loading)

            expenseTransactionRepo.getAllExpensesByMonth(month, year)
                .distinctUntilChanged()
                .collectLatest { result ->
                    result.onSuccess { expenseList ->
                        val totalExpense = expenseList.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
                        _reportUiState.update {
                            it.copy(
                                totalExpense = totalExpense,
                            )
                        }
                        _allExpenseByMonth.value = expenseList
                        Log.d("TAG", "getAllExpenseByMonth: ${allExpenseByMonth.value}")
                        _reportUiEvent.emit(ReportUiEvent.Success("getAllExpenseByMonth Loaded"))
                    }.onFailure {
                        _reportUiEvent.emit(ReportUiEvent.Success("getAllExpenseByMonth Failed"))
                    }
                }
        }
    }
}


sealed class ReportUiEvent() {
    object Ideal : ReportUiEvent()
    object Loading : ReportUiEvent()
    data class Success(val message: String) : ReportUiEvent()
    data class Error(val message: String) : ReportUiEvent()
}


data class ReportUiState(
    val selectedMonth : Int = LocalDate.now().monthValue,
    val selectedYear: Int = LocalDate.now().year,
    val totalExpense: Double = 0.0,
)
