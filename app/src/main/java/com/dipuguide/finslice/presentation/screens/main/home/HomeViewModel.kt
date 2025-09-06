package com.dipuguide.finslice.presentation.screens.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dipuguide.finslice.domain.model.ExpenseTransaction
import com.dipuguide.finslice.domain.model.IncomeTransaction
import com.dipuguide.finslice.domain.model.User
import com.dipuguide.finslice.domain.repo.ExpenseTransactionRepo
import com.dipuguide.finslice.domain.repo.IncomeTransactionRepo
import com.dipuguide.finslice.domain.repo.UserAuthRepository
import com.dipuguide.finslice.presentation.common.state.UiState
import com.dipuguide.finslice.utils.formatNumberToIndianStyle
import dagger.hilt.android.lifecycle.HiltViewModel
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
class HomeViewModel @Inject constructor(
    private val incomeTransactionRepo: IncomeTransactionRepo,
    private val expenseTransactionRepo: ExpenseTransactionRepo,
    private val userAuthRepository: UserAuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState = _homeUiState.asStateFlow()

    private val _homeNavigation = MutableSharedFlow<HomeNavigation>()
    val homeNavigation = _homeNavigation.asSharedFlow()

    private val _allExpensesByCategory = MutableStateFlow<Map<String, List<ExpenseTransaction>>>(emptyMap())
    val allExpensesByCategory = _allExpensesByCategory.asStateFlow()

    private val _allIncomeTransactions = MutableStateFlow<List<IncomeTransaction>>(emptyList())
    val allIncomeTransactions = _allIncomeTransactions.asStateFlow()

    private val _allExpenseTransactions = MutableStateFlow<List<ExpenseTransaction>>(emptyList())
    val allExpenseTransactions = _allExpenseTransactions.asStateFlow()

    private val _userDetail = MutableStateFlow(User())
    val userDetail = _userDetail.asStateFlow()

    init {
        fetchAllHomeData()
        getUserDetail()
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.MonthPickerClick -> setMonthAndYear(event.month, event.year)
            HomeUiEvent.MonthOverviewClick -> {
                viewModelScope.launch { _homeNavigation.emit(HomeNavigation.REPORT) }
            }
        }
    }

    private fun setMonthAndYear(month: Int, year: Int) {
        _homeUiState.update { it.copy(selectedMonth = month, selectedYear = year) }
        fetchAllHomeData()
    }

    private fun fetchAllHomeData() {
        val month = homeUiState.value.selectedMonth
        val year = homeUiState.value.selectedYear
        getAllExpenseTransaction(month, year)
        getAllIncomeTransaction(month, year)
    }

    private fun getUserDetail() {
        viewModelScope.launch {
            userAuthRepository.getUserDetails().collectLatest { result ->
                result.onSuccess { user ->
                    _userDetail.update {
                        it.copy(
                            name = user.name,
                            email = user.email,
                            photoUri = user.photoUri
                        )
                    }
                }
                result.onFailure {
                    // TODO: handle error (log / show toast via event)
                }
            }
        }
    }

    fun getAllIncomeTransaction(month: Int, year: Int) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            incomeTransactionRepo.getAllIncomeByMonth(month, year)
                .collectLatest { result ->
                    result.onSuccess { incomeTransactions ->
                        _allIncomeTransactions.value = incomeTransactions
                        calculateTotalIncome()
                        calculateIncomeAverage()
                        calculatePlannedPercentages()
                        calculateNetAmount()
                        _uiState.value = UiState.Success("Monthly income loaded")
                        _uiState.value = UiState.Idle
                    }.onFailure {
                        _uiState.value = UiState.Error("Failed to load income")
                    }
                }
        }
    }

    fun getAllExpenseTransaction(month: Int, year: Int) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            expenseTransactionRepo.getAllExpensesByMonth(month, year)
                .collectLatest { result ->
                    result.onSuccess { expenseTransactions ->
                        _allExpenseTransactions.value = expenseTransactions
                        calculateTotalExpense()
                        calculateExpensesByCategory(expenseTransactions)
                        calculateNetAmount()
                        _uiState.value = UiState.Success("Monthly expenses loaded")
                        _uiState.value = UiState.Idle
                    }.onFailure {
                        _uiState.value = UiState.Error("Failed to load expenses")
                    }
                }
        }
    }

    private fun calculateExpensesByCategory(expenses: List<ExpenseTransaction>) {
        val grouped = expenses.groupBy { it.category }
        _allExpensesByCategory.value = grouped

        _homeUiState.update { state ->
            state.copy(
                needExpenseTotal = formatNumberToIndianStyle(grouped["Need"]?.sumOf { it.amount } ?: 0.0),
                wantExpenseTotal = formatNumberToIndianStyle(grouped["Want"]?.sumOf { it.amount } ?: 0.0),
                investExpenseTotal = formatNumberToIndianStyle(grouped["Invest"]?.sumOf { it.amount } ?: 0.0)
            )
        }
    }

    private fun calculatePlannedPercentages() {
        val totalIncome = _allIncomeTransactions.value.sumOf { it.amount }
        _homeUiState.update {
            it.copy(
                needPercentageAmount = formatNumberToIndianStyle(totalIncome * 0.5),
                wantPercentageAmount = formatNumberToIndianStyle(totalIncome * 0.3),
                investPercentageAmount = formatNumberToIndianStyle(totalIncome * 0.2)
            )
        }
    }

    private fun calculateIncomeAverage() {
        val transactions = _allIncomeTransactions.value
        if (transactions.isNotEmpty()) {
            val avg = transactions.sumOf { it.amount } / transactions.size
            _homeUiState.update { it.copy(averageIncome = formatNumberToIndianStyle(avg)) }
        }
    }

    private fun calculateNetAmount() {
        val income = _homeUiState.value.totalIncome.replace(",", "").toDoubleOrNull() ?: 0.0
        val expense = _homeUiState.value.totalExpense.replace(",", "").toDoubleOrNull() ?: 0.0
        _homeUiState.update { it.copy(netAmount = formatNumberToIndianStyle(income - expense)) }
    }

    private fun calculateTotalExpense() {
        val total = _allExpenseTransactions.value.sumOf { it.amount }
        _homeUiState.update { it.copy(totalExpense = formatNumberToIndianStyle(total)) }
    }

    private fun calculateTotalIncome() {
        val total = _allIncomeTransactions.value.sumOf { it.amount }
        _homeUiState.update { it.copy(totalIncome = formatNumberToIndianStyle(total)) }
    }
}
