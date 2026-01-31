package com.dipuguide.finslice.presentation.screens.main.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dipuguide.finslice.domain.model.ExpenseTransaction
import com.dipuguide.finslice.domain.repo.ExpenseTransactionRepo
import com.dipuguide.finslice.presentation.common.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class ReportUiState(
    val selectedMonth: Int = java.time.LocalDate.now().monthValue,
    val selectedYear: Int = java.time.LocalDate.now().year,
    val totalExpense: Double = 0.0,
    val expensesByTag: List<TagExpense> = emptyList()
)

data class TagExpense(
    val tag: String,
    val totalAmount: Double,
    val percentage: Double
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ReportViewModel @Inject constructor(
    private val expenseTransactionRepo: ExpenseTransactionRepo,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _selectedDate = MutableStateFlow(Pair(java.time.LocalDate.now().monthValue, java.time.LocalDate.now().year))

    val expenseList: StateFlow<List<ExpenseTransaction>> = _selectedDate
        .flatMapLatest { (month, year) ->
            expenseTransactionRepo.getAllExpensesByMonth(month, year)
        }
        .map { result ->
            result.getOrNull() ?: emptyList()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val reportUiState: StateFlow<ReportUiState> = kotlinx.coroutines.flow.combine(
        _selectedDate,
        expenseList
    ) { (month, year), list ->
        val totalAmount = list.sumOf { it.amount }
        val grouped = list.groupBy { it.tag ?: "Untagged" }
            .map { (tag, expenses) ->
                val tagTotal = expenses.sumOf { it.amount }
                TagExpense(
                    tag = tag,
                    totalAmount = tagTotal,
                    percentage = if (totalAmount > 0) (tagTotal / totalAmount) * 100.0 else 0.0
                )
            }
            .sortedByDescending { it.totalAmount }

        ReportUiState(
            selectedMonth = month,
            selectedYear = year,
            totalExpense = totalAmount,
            expensesByTag = grouped
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ReportUiState())

    fun onMonthYearSelected(month: Int, year: Int) {
        _selectedDate.value = Pair(month, year)
    }

    fun resetUiState() {
        _uiState.value = UiState.Idle
    }
}

