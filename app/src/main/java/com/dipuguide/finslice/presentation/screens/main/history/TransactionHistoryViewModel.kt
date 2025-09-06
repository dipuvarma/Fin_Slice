package com.dipuguide.finslice.presentation.screens.main.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dipuguide.finslice.domain.model.ExpenseTransaction
import com.dipuguide.finslice.domain.model.IncomeTransaction
import com.dipuguide.finslice.domain.repo.ExpenseTransactionRepo
import com.dipuguide.finslice.domain.repo.IncomeTransactionRepo
import com.dipuguide.finslice.presentation.common.state.UiState
import com.dipuguide.finslice.utils.DateFilterType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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


    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _selectedTab = MutableStateFlow(0)
    val selectedTab = _selectedTab.asStateFlow()

    private val _getAllExpenseByDate = MutableStateFlow<List<ExpenseTransaction>>(emptyList())
    val getAllExpenseByDate = _getAllExpenseByDate.asStateFlow()

    private val _getAllIncomeByDate = MutableStateFlow<List<IncomeTransaction>>(emptyList())
    val getAllIncomeByDate = _getAllIncomeByDate.asStateFlow()

    private val _selectedFilter = MutableStateFlow<DateFilterType>(DateFilterType.Today)
    val selectedFilter: StateFlow<DateFilterType> = _selectedFilter.asStateFlow()

    init {
        fetchAllForFilter(DateFilterType.Today)
    }

    fun onTabSelected(index: Int) {
        _selectedTab.value = index
    }

    fun onFilterSelected(filter: DateFilterType) {
        _selectedFilter.value = filter
        fetchAllForFilter(filter)
    }

    fun fetchAllForFilter(filter: DateFilterType) {
        getAllExpensesByDateRange(filter)
        getIncomeTransactionByDate(filter)
    }

    private fun getAllExpensesByDateRange(
        filter: DateFilterType,
    ) {
        viewModelScope.launch {

            _uiState.value = UiState.Loading

            expenseTransactionRepo.getAllExpensesByDateRange(filter)
                .distinctUntilChanged()
                .collectLatest { result ->
                    result.onSuccess { data ->
                        _getAllExpenseByDate.value = data
                        _uiState.value = UiState.Success("getAllExpensesByDateRange")
                        _uiState.value = UiState.Idle
                    }
                    result.onFailure { error ->
                        _uiState.value = UiState.Error(
                            error.message ?: "Unknown error fetching expenses for $filter"
                        )
                        _uiState.value = UiState.Idle
                    }
                }
        }
    }

    private fun getIncomeTransactionByDate(
        filter: DateFilterType,
    ) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            incomeTransactionRepo.getIncomeTransactionByDate(filter)
                .distinctUntilChanged()
                .collectLatest { result ->
                    result.onSuccess { data ->
                        _getAllIncomeByDate.value = data
                        _uiState.value = UiState.Success("getIncomeTransactionByDate")
                        _uiState.value = UiState.Idle
                    }
                    result.onFailure { error ->
                        _uiState.value = UiState.Error(
                            error.message ?: "Unknown error fetching expenses for $filter"
                        )
                        _uiState.value = UiState.Idle
                    }
                }
        }
    }

    fun editExpenseTransaction(expenseTransactionUi: ExpenseTransaction) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = expenseTransactionRepo.editExpenseTransaction(expenseTransactionUi)
            result.onSuccess {
                _uiState.value = UiState.Success("Edit Success")
            }
            result.onFailure {
                _uiState.value = UiState.Error(it.message ?: "Edit Failed")
            }
        }
    }

    fun deleteExpenseTransaction(expenseId: String) {
        viewModelScope.launch {

            _uiState.value = UiState.Loading

            val result = expenseTransactionRepo.deleteExpenseTransaction(expenseId = expenseId)

            result.onSuccess {
                _uiState.value = UiState.Success("Updated SuccessFully")
            }

            result.onFailure {
                _uiState.value = UiState.Error(it.message ?: "Update Failed")
            }
        }
    }

    fun deleteIncomeTransaction(incomeId: String) {
        viewModelScope.launch {

            _uiState.value = UiState.Loading

            val result = incomeTransactionRepo.deleteIncomeTransaction(transactionId = incomeId)

            result.onSuccess {
                _uiState.value = UiState.Success("Income deleted successfully")
            }

            result.onFailure {
                _uiState.value = UiState.Error(it.message ?: "Failed to delete income")
            }
        }
    }

    fun resetUiState(){
        _uiState.value = UiState.Idle
    }
}
