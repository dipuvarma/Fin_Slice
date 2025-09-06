package com.dipuguide.finslice.presentation.screens.main.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dipuguide.finslice.domain.model.ExpenseTransaction
import com.dipuguide.finslice.domain.repo.ExpenseTransactionRepo
import com.dipuguide.finslice.presentation.common.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

data class ReportUiState(
    val selectedMonth: Int = LocalDate.now().monthValue,
    val selectedYear: Int = LocalDate.now().year,
    val totalExpense: Double = 0.0,
)

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val expenseTransactionRepo: ExpenseTransactionRepo,
) : ViewModel() {

    // Expose immutable flows to the UI.
    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _reportUiState = MutableStateFlow(ReportUiState())
    val reportUiState: StateFlow<ReportUiState> = _reportUiState.asStateFlow()


    private val _expenseList = MutableStateFlow<List<ExpenseTransaction>>(emptyList())
    val expenseList: StateFlow<List<ExpenseTransaction>> = _expenseList.asStateFlow()

    init {
        // initialize with current month/year
        loadExpensesForMonth(LocalDate.now().monthValue, LocalDate.now().year)
    }

    fun onMonthYearSelected(month: Int, year: Int) {
        // update UI selection and fetch data. Keep state update separate so UI can react immediately.
        _reportUiState.update { it.copy(selectedMonth = month, selectedYear = year) }
        loadExpensesForMonth(month, year)
    }

    private fun loadExpensesForMonth(month: Int, year: Int) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            Timber.d("Loading expenses for %d-%d", month, year)

            expenseTransactionRepo.getAllExpensesByMonth(month, year)
                .distinctUntilChanged()
                .catch { e ->
                    Timber.e(e, "Failed to get expenses for %d-%d", month, year)
                    _uiState.value = UiState.Error("Failed to load expenses")
                }
                .collectLatest { result ->
                    result.onSuccess { list ->
                        Timber.d("Fetched %d expenses", list.size)
                        _expenseList.value = list

                        // compute total once and update state
                        val total = list.sumOf { it.amount }
                        _reportUiState.update { it.copy(totalExpense = total) }

                        _uiState.value = UiState.Success("Expenses loaded")
                        // Do NOT immediately flip to Idle here; let the UI clear it via resetUiState
                    }.onFailure { throwable ->
                        Timber.e(throwable, "Repository returned failure")
                        _uiState.value = UiState.Error("Failed to load expenses")
                    }
                }
        }
    }

    fun resetUiState() {
        _uiState.value = UiState.Idle
    }

}

