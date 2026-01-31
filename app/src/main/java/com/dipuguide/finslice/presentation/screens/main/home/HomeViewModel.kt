package com.dipuguide.finslice.presentation.screens.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dipuguide.finslice.domain.model.User
import com.dipuguide.finslice.domain.repo.ExpenseTransactionRepo
import com.dipuguide.finslice.domain.repo.IncomeTransactionRepo
import com.dipuguide.finslice.domain.repo.UserAuthRepository
import com.dipuguide.finslice.presentation.common.state.UiState
import com.dipuguide.finslice.utils.formatNumberToIndianStyle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val incomeTransactionRepo: IncomeTransactionRepo,
    private val expenseTransactionRepo: ExpenseTransactionRepo,
    private val userAuthRepository: UserAuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _selectedDate = MutableStateFlow(Pair(currentMonth(), currentYear()))
    
    private val _homeNavigation = MutableSharedFlow<HomeNavigation>()
    val homeNavigation = _homeNavigation.asSharedFlow()

    val userDetail: StateFlow<User> = userAuthRepository.getUserDetails()
        .flatMapLatest { result ->
            val user = result.getOrNull() ?: User()
            kotlinx.coroutines.flow.flowOf(user)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), User())

    private val incomeFlow = _selectedDate.flatMapLatest { (month, year) ->
        incomeTransactionRepo.getAllIncomeByMonth(month, year)
    }

    private val expenseFlow = _selectedDate.flatMapLatest { (month, year) ->
        expenseTransactionRepo.getAllExpensesByMonth(month, year)
    }

    val homeUiState: StateFlow<HomeUiState> = combine(
        _selectedDate,
        incomeFlow,
        expenseFlow
    ) { date, incomeResult, expenseResult ->
        
        // Handle UI State (Error/Loading)
        when {
            incomeResult.isFailure || expenseResult.isFailure -> {
                _uiState.value = UiState.Error("Failed to load financial data")
            }
            else -> {
                _uiState.value = UiState.Idle
            }
        }

        val incomeList = incomeResult.getOrNull() ?: emptyList()
        val expenseList = expenseResult.getOrNull() ?: emptyList()
        
        val totalIncome = incomeList.sumOf { it.amount }
        val totalExpense = expenseList.sumOf { it.amount }
        val groupedExpenses = expenseList.groupBy { it.category }

        HomeUiState(
            selectedMonth = date.first,
            selectedYear = date.second,
            totalIncome = formatNumberToIndianStyle(totalIncome),
            totalExpense = formatNumberToIndianStyle(totalExpense),
            netAmount = formatNumberToIndianStyle(totalIncome - totalExpense),
            averageIncome = if (incomeList.isNotEmpty()) formatNumberToIndianStyle(totalIncome / incomeList.size) else "0",
            needExpenseTotal = formatNumberToIndianStyle(groupedExpenses["Need"]?.sumOf { it.amount } ?: 0.0),
            wantExpenseTotal = formatNumberToIndianStyle(groupedExpenses["Want"]?.sumOf { it.amount } ?: 0.0),
            investExpenseTotal = formatNumberToIndianStyle(groupedExpenses["Invest"]?.sumOf { it.amount } ?: 0.0),
            needPercentageAmount = formatNumberToIndianStyle(totalIncome * 0.5),
            wantPercentageAmount = formatNumberToIndianStyle(totalIncome * 0.3),
            investPercentageAmount = formatNumberToIndianStyle(totalIncome * 0.2)
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState())

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.MonthPickerClick -> {
                _selectedDate.value = Pair(event.month, event.year)
            }
            HomeUiEvent.MonthOverviewClick -> {
                viewModelScope.launch { _homeNavigation.emit(HomeNavigation.REPORT) }
            }
        }
    }
}

private fun currentMonth() = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH) + 1
private fun currentYear() = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
