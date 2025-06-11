package com.dipuguide.finslice.presentation.screens.main.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dipuguide.finslice.data.repo.ExpenseTransactionRepo
import com.dipuguide.finslice.data.repo.IncomeTransactionRepo
import com.dipuguide.finslice.presentation.screens.main.transaction.ExpenseTransactionUi
import com.dipuguide.finslice.presentation.screens.main.transaction.IncomeTransactionUi
import com.dipuguide.finslice.utils.formatNumberToIndianStyle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val incomeTransactionRepo: IncomeTransactionRepo,
    private val expenseTransactionRepo: ExpenseTransactionRepo,
) : ViewModel() {

    companion object {
        const val TAG = "HomeViewModel"
    }

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState = _homeUiState.asStateFlow()

    private val _homeUiEvent = MutableSharedFlow<HomeUiEvent>()
    val homeUiEvent = _homeUiEvent.asSharedFlow()

    private val _allExpensesByCategory = MutableStateFlow<List<ExpenseTransactionUi>>(emptyList())
    val allExpensesByCategory = _allExpensesByCategory.asStateFlow()

    private val _allIncomeList = MutableStateFlow<List<IncomeTransactionUi>>(emptyList())
    val allIncomeList = _allIncomeList.asStateFlow()

    private val _allExpenseList = MutableStateFlow<List<ExpenseTransactionUi>>(emptyList())
    val allExpenseList = _allExpenseList.asStateFlow()

    private var isIncomeLoaded = false
    private var isExpenseLoaded = false

    init {
        getAllExpenseTransaction()
        getAllIncomeTransaction()
        getAllExpensesByCategory("Need")
        getAllExpensesByCategory("Want")
        getAllExpensesByCategory("Invest")
    }

    private fun tryCalculateNetAmount() {
        if (isIncomeLoaded && isExpenseLoaded) {
            calculateNetAmount()
        }
    }

    private fun calculateNetAmount() {
        val income = _homeUiState.value.totalIncome.replace(",", "").toDoubleOrNull() ?: 0.0
        val expense = _homeUiState.value.totalExpense.replace(",", "").toDoubleOrNull() ?: 0.0
        val net = income - expense

        Log.d(TAG, "calculateNetAmount - Income: $income, Expense: $expense, Net: $net")

        _homeUiState.update {
            it.copy(netBalance = formatNumberToIndianStyle(net))
        }
    }

    private fun calculateTotalExpense() {
        val list = _allExpenseList.value
        val total = list.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }

        Log.d(TAG, "calculateTotalExpense - Total: $total")

        _homeUiState.update {
            it.copy(totalExpense = formatNumberToIndianStyle(total))
        }
    }

    private fun calculateTotalIncome() {
        val list = _allIncomeList.value
        val total = list.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }

        Log.d(TAG, "calculateTotalIncome - Total: $total")

        _homeUiState.update {
            it.copy(totalIncome = formatNumberToIndianStyle(total))
        }
    }

    private fun calculateIncomeAverage() {
        val transactions = _allIncomeList.value
        if (transactions.isNotEmpty()) {
            val avg = transactions.sumOf { it.amount.toDoubleOrNull() ?: 0.0 } / transactions.size
            Log.d(TAG, "calculateIncomeAverage - Avg: $avg")
            _homeUiState.update {
                it.copy(averageIncome = formatNumberToIndianStyle(avg))
            }
        }
    }

    private fun calculatePercentageAmount(label: String, percentage: Double) {
        val totalIncome = _allIncomeList.value.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
        val amount = formatNumberToIndianStyle(totalIncome * percentage)

        _homeUiState.update {
            when (label) {
                "Need" -> it.copy(needPercentageAmount = amount)
                "Want" -> it.copy(wantPercentageAmount = amount)
                "Invest" -> it.copy(investPercentageAmount = amount)
                else -> it
            }
        }
    }

    fun getAllExpensesByCategory(category: String) {
        viewModelScope.launch {
            _homeUiEvent.emit(HomeUiEvent.Loading)

            expenseTransactionRepo.getAllExpensesByCategory(category).distinctUntilChanged().collectLatest { result ->
                result.onSuccess { data ->
                    val total = data.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
                    _allExpensesByCategory.value = data

                    Log.d(TAG, "Expenses by $category - Count: ${data.size}, Total: $total")

                    _homeUiState.update {
                        when (category) {
                            "Need" -> it.copy(needExpenseTotal = formatNumberToIndianStyle(total))
                            "Want" -> it.copy(wantExpenseTotal = formatNumberToIndianStyle(total))
                            "Invest" -> it.copy(investExpenseTotal = formatNumberToIndianStyle(total))
                            else -> it
                        }
                    }

                    _homeUiEvent.emit(HomeUiEvent.Success("Expenses loaded By Category"))
                }.onFailure {
                    Log.e(TAG, "getAllExpensesByCategory Failed", it)
                    _homeUiEvent.emit(HomeUiEvent.Error("Get All Expense Transaction By Category Failed"))
                }
            }
        }
    }

    fun getAllIncomeTransaction() {
        viewModelScope.launch {
            _homeUiEvent.emit(HomeUiEvent.Loading)

            incomeTransactionRepo.getIncomeTransaction().distinctUntilChanged().collectLatest { result ->
                result.onSuccess { data ->
                    _allIncomeList.value = data

                    Log.d(TAG, "getAllIncomeTransaction - Loaded: ${data.size}")

                    calculateTotalIncome()
                    calculateIncomeAverage()
                    calculatePercentageAmount("Need", 0.5)
                    calculatePercentageAmount("Want", 0.3)
                    calculatePercentageAmount("Invest", 0.2)
                    isIncomeLoaded = true
                    tryCalculateNetAmount()

                    _homeUiEvent.emit(HomeUiEvent.Success("AllIncomeList Loaded"))
                }.onFailure {
                    Log.e(TAG, "getAllIncomeTransaction Failed", it)
                    _homeUiEvent.emit(HomeUiEvent.Error("AllIncomeList Failed"))
                }
            }
        }
    }

    fun getAllExpenseTransaction() {
        viewModelScope.launch {
            _homeUiEvent.emit(HomeUiEvent.Loading)

            expenseTransactionRepo.getExpenseTransaction().distinctUntilChanged().collectLatest { result ->
                result.onSuccess { data ->
                    _allExpenseList.value = data

                    Log.d(TAG, "getAllExpenseTransaction - Loaded: ${data.size}")

                    calculateTotalExpense()
                    isExpenseLoaded = true
                    tryCalculateNetAmount()

                    _homeUiEvent.emit(HomeUiEvent.Success("AllExpenseList Loaded"))
                }.onFailure {
                    Log.e(TAG, "getAllExpenseTransaction Failed", it)
                    _homeUiEvent.emit(HomeUiEvent.Error("AllExpenseList Failed"))
                }
            }
        }
    }
}
