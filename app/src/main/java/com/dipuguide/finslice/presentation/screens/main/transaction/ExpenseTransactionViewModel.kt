package com.dipuguide.finslice.presentation.screens.main.transaction

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dipuguide.finslice.data.repo.ExpenseTransactionRepo
import com.dipuguide.finslice.utils.Category
import com.dipuguide.finslice.utils.DateFilterType
import com.dipuguide.finslice.utils.formatNumberToIndianStyle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseTransactionViewModel @Inject constructor(
    private val expenseTransactionRepo: ExpenseTransactionRepo,
) : ViewModel() {

    //for event
    private val _expenseUiEvent = MutableSharedFlow<ExpenseTransactionUiEvent>()
    val expenseEvent = _expenseUiEvent.asSharedFlow()

    //expense TransactionUI
    private val _expenseUiState = MutableStateFlow(ExpenseTransactionUi())
    val expenseUiState = _expenseUiState.asStateFlow()

    // All Expense Transaction
    private val _allExpenseUiState = MutableStateFlow(AllExpenseUiState())
    val allExpenseUiState = _allExpenseUiState.asStateFlow()

    private val _getAllExpenseByCategory = MutableStateFlow(AllExpenseUiState())
    val getAllExpenseByCategory = _getAllExpenseByCategory.asStateFlow()

    private val _getAllExpenseByDate = MutableStateFlow<List<ExpenseTransactionUi>>(emptyList())
    val getAllExpenseByDate = _getAllExpenseByDate.asStateFlow()

    // 👇 Persistent selected filter state
    private val _selectedFilter = MutableStateFlow<DateFilterType>(DateFilterType.Today)
    val selectedFilter: StateFlow<DateFilterType> = _selectedFilter.asStateFlow()



    fun addExpenseTransaction(expenseTransactionUi: ExpenseTransactionUi) {
        viewModelScope.launch {
            _expenseUiEvent.emit(ExpenseTransactionUiEvent.Loading)
            val result = expenseTransactionRepo.addExpenseTransaction(expenseTransactionUi)
            result.onSuccess {
                Log.d("addExpenseTransaction ", "Add Expense Successfully")
                _expenseUiEvent.emit(ExpenseTransactionUiEvent.Success("Add Expense Successfully"))
            }.onFailure {
                Log.e("addExpenseTransaction ", "Add Expense Failed", it)
                _expenseUiEvent.emit(ExpenseTransactionUiEvent.Error("Add Expense Failed"))
            }
        }
    }




    fun getExpenseTransaction() {
        viewModelScope.launch {
            _expenseUiEvent.emit(ExpenseTransactionUiEvent.Loading)

            expenseTransactionRepo.getExpenseTransaction().distinctUntilChanged().collectLatest { result ->

                result.onSuccess { expenseTransactionList ->
                    Log.d("calculateTotalExpense", "${expenseTransactionList.size}")

                    _allExpenseUiState.update {
                        it.copy(
                            expenseTransactionList = expenseTransactionList
                        )
                    }
                    Log.d(
                        "getExpenseTransaction",
                        "getExpenseTransaction: ${expenseTransactionList.size}"
                    )
                }
                result.onFailure {
                    Log.e("getExpenseTransaction", "getExpenseTransaction Failed", it)
                }
            }
        }
    }


    fun getAllExpensesByCategory(category: String) {
        viewModelScope.launch {

            _expenseUiEvent.emit(ExpenseTransactionUiEvent.Loading)

            expenseTransactionRepo.getAllExpensesByCategory(category).distinctUntilChanged().collectLatest { result ->
                result.onSuccess { data ->
                    _getAllExpenseByCategory.update {
                        it.copy(
                            expenseTransactionList = data,
                        )
                    }
                    _expenseUiEvent.emit(ExpenseTransactionUiEvent.Success("Expenses loaded By Category"))
                    Log.d(
                        "getAllExpensesByCategory",
                        "getAllExpensesByCategory: ${data.size}"
                    )
                }
                result.onFailure {
                    Log.e("getAllExpensesByCategory", "getAllExpensesByCategory Failed", it)
                    _expenseUiEvent.emit(ExpenseTransactionUiEvent.Error("Get All Expense Transaction By Category Failed"))
                }
            }
        }
    }


    fun getAllExpensesByDateRange(filter: DateFilterType) {
        viewModelScope.launch {
            _expenseUiEvent.emit(ExpenseTransactionUiEvent.Loading)
            expenseTransactionRepo.getAllExpensesByDateRange(filter).distinctUntilChanged().collectLatest { result ->
                result.onSuccess { data ->
                    _getAllExpenseByDate.value = data
                    _expenseUiEvent.emit(ExpenseTransactionUiEvent.Success("Expenses loaded"))
                }
                result.onFailure { error ->
                    _expenseUiEvent.emit(
                        ExpenseTransactionUiEvent.Error(
                            error.message ?: "Unknown error"
                        )
                    )
                }

            }
        }
    }


    fun editExpenseTransaction(expenseTransactionUi: ExpenseTransactionUi) {

        viewModelScope.launch {
            _expenseUiEvent.emit(ExpenseTransactionUiEvent.Loading)

            val result = expenseTransactionRepo.editExpenseTransaction(expenseTransactionUi)

            result.onSuccess {
                _expenseUiEvent.emit(ExpenseTransactionUiEvent.Success("Edit Expense Successfully"))

            }.onFailure {
                _expenseUiEvent.emit(ExpenseTransactionUiEvent.Error("Edit Expense Failed"))
            }
        }
    }

    fun deleteExpenseTransaction(id: String) {
        viewModelScope.launch {
            _expenseUiEvent.emit(ExpenseTransactionUiEvent.Loading)

            val result = expenseTransactionRepo.deleteExpenseTransaction(id)

            result.onSuccess {
                _expenseUiEvent.emit(ExpenseTransactionUiEvent.Success("Delete Expense Successfully"))

            }.onFailure {
                _expenseUiEvent.emit(ExpenseTransactionUiEvent.Error("Delete Expense Failed"))
            }
        }
    }

}