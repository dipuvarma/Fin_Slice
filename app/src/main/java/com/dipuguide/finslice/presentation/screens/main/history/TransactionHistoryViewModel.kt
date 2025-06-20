package com.dipuguide.finslice.presentation.screens.main.history

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dipuguide.finslice.data.repo.ExpenseTransactionRepo
import com.dipuguide.finslice.data.repo.IncomeTransactionRepo
import com.dipuguide.finslice.presentation.model.ExpenseTransactionUi
import com.dipuguide.finslice.presentation.model.IncomeTransactionUi
import com.dipuguide.finslice.utils.DateFilterType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TransactionHistoryViewModel @Inject constructor(
    private val expenseTransactionRepo: ExpenseTransactionRepo,
    private val incomeTransactionRepo: IncomeTransactionRepo,
) : ViewModel() {

    companion object {
        private const val TAG = "TransactionHistoryViewModel"
    }

    var selectedTab by mutableIntStateOf(0)

    private val _expenseHistoryUiEvent = MutableSharedFlow<ExpenseHistoryUiEvent>()
    val expenseHistoryUiEvent: SharedFlow<ExpenseHistoryUiEvent> =
        _expenseHistoryUiEvent.asSharedFlow()

    private val _getAllExpenseByDate = MutableStateFlow<List<ExpenseTransactionUi>>(emptyList())
    val getAllExpenseByDate: StateFlow<List<ExpenseTransactionUi>> =
        _getAllExpenseByDate.asStateFlow()

    private val _getAllIncomeByDate = MutableStateFlow<List<IncomeTransactionUi>>(emptyList())
    val getAllIncomeByDate: StateFlow<List<IncomeTransactionUi>> = _getAllIncomeByDate.asStateFlow()

    private val _incomeHistoryUiEvent = MutableSharedFlow<IncomeHistoryUiEvent>()
    val incomeHistoryUiEvent: SharedFlow<IncomeHistoryUiEvent> =
        _incomeHistoryUiEvent.asSharedFlow()

    private val _selectedFilter = MutableStateFlow<DateFilterType>(DateFilterType.Today)
    val selectedFilter: StateFlow<DateFilterType> = _selectedFilter.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        Log.d(TAG, "Initializing with default filter: Today")
        fetchAllForFilter(DateFilterType.Today)
    }

    fun onTabSelected(index: Int) {
        Log.d(TAG, "Tab selected: $index")
        selectedTab = index
    }

    fun onFilterSelected(filter: DateFilterType) {
        Log.d(TAG, "Filter selected: $filter")
        _selectedFilter.value = filter
        fetchAllForFilter(filter)
    }

    fun refresh() {
        Log.d(TAG, "Refreshing data for current filter: ${selectedFilter.value}")
        fetchAllForFilter(selectedFilter.value, isManualRefresh = true)
    }

    private fun fetchAllForFilter(
        filter: DateFilterType,
        isManualRefresh: Boolean = false,
    ) {
        getAllExpensesByDateRange(filter, isManualRefresh)
        getIncomeTransactionByDate(filter, isManualRefresh)
    }

    private fun getAllExpensesByDateRange(
        filter: DateFilterType,
        isManualRefresh: Boolean = false,
    ) {
        viewModelScope.launch {
            if (isManualRefresh) _isRefreshing.value = true

            _expenseHistoryUiEvent.emit(ExpenseHistoryUiEvent.Loading)
            Log.d(TAG, "Fetching expenses for $filter")

            expenseTransactionRepo.getAllExpensesByDateRange(filter)
                .distinctUntilChanged()
                .collectLatest { result ->
                    result.onSuccess { data ->
                        Log.d(TAG, "Fetched ${data.size} expense(s) for $filter")
                        _getAllExpenseByDate.value = data
                        _expenseHistoryUiEvent.emit(ExpenseHistoryUiEvent.Success("Expenses loaded for $filter"))
                    }
                    result.onFailure { error ->
                        Log.e(TAG, "Error fetching expenses for $filter", error)
                        _expenseHistoryUiEvent.emit(
                            ExpenseHistoryUiEvent.Error(
                                error.message ?: "Unknown error fetching expenses for $filter"
                            )
                        )
                    }

                    if (isManualRefresh) _isRefreshing.value = false
                }
        }
    }

    private fun getIncomeTransactionByDate(
        filter: DateFilterType,
        isManualRefresh: Boolean = false,
    ) {
        viewModelScope.launch {
            if (isManualRefresh) _isRefreshing.value = true

            _incomeHistoryUiEvent.emit(IncomeHistoryUiEvent.Loading)
            Log.d(TAG, "Fetching income for $filter")

            incomeTransactionRepo.getIncomeTransactionByDate(filter)
                .distinctUntilChanged()
                .collectLatest { result ->
                    result.onSuccess { data ->
                        Log.d(TAG, "Fetched ${data.size} income record(s) for $filter")
                        _getAllIncomeByDate.value = data
                        _incomeHistoryUiEvent.emit(IncomeHistoryUiEvent.Success("Income loaded for $filter"))
                    }
                    result.onFailure { error ->
                        Log.e(TAG, "Error fetching income for $filter", error)
                        _incomeHistoryUiEvent.emit(
                            IncomeHistoryUiEvent.Error(
                                error.message ?: "Unknown error fetching income for $filter"
                            )
                        )
                    }

                    if (isManualRefresh) _isRefreshing.value = false
                }
        }
    }

    fun editExpenseTransaction(expenseTransactionUi: ExpenseTransactionUi) {
        viewModelScope.launch {
            _expenseHistoryUiEvent.emit(ExpenseHistoryUiEvent.Loading)

            val result = expenseTransactionRepo.editExpenseTransaction(
                expenseTransactionUi
            )

            result.onSuccess {
                _expenseHistoryUiEvent.emit(ExpenseHistoryUiEvent.Success("Edit Success"))
            }

            result.onFailure {
                _expenseHistoryUiEvent.emit(ExpenseHistoryUiEvent.Error("Edit Failed"))
            }
        }
    }

    fun deleteExpenseTransaction(id: String) {
        viewModelScope.launch {
            _expenseHistoryUiEvent.emit(ExpenseHistoryUiEvent.Loading)
            val result = expenseTransactionRepo.deleteExpenseTransaction(id = id)

            result.onSuccess {
                _expenseHistoryUiEvent.emit(ExpenseHistoryUiEvent.Success("Updated SuccessFully"))
            }

            result.onFailure {
                _expenseHistoryUiEvent.emit(ExpenseHistoryUiEvent.Error("Update Failed"))
            }
        }
    }

    fun deleteIncomeTransaction(id: String){
        viewModelScope.launch {
            _expenseHistoryUiEvent.emit(ExpenseHistoryUiEvent.Loading)
            val result = incomeTransactionRepo.deleteIncomeTransaction(id = id)

            result.onSuccess {
                _expenseHistoryUiEvent.emit(ExpenseHistoryUiEvent.Success("Updated SuccessFully"))
            }

            result.onFailure {
                _expenseHistoryUiEvent.emit(ExpenseHistoryUiEvent.Error("Update Failed"))
            }
        }
    }
}
