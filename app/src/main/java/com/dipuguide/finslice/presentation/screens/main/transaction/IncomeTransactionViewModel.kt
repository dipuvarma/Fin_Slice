package com.dipuguide.finslice.presentation.screens.main.transaction

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dipuguide.finslice.data.repo.IncomeTransactionRepo
import com.dipuguide.finslice.utils.DateFilterType
import com.dipuguide.finslice.utils.formatNumberToIndianStyle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IncomeTransactionViewModel @Inject constructor(
    private val incomeTransactionRepo: IncomeTransactionRepo,
) : ViewModel() {

    private val _incomeUiState = MutableStateFlow(IncomeTransactionUiState())
    val incomeUiState = _incomeUiState.asStateFlow()


    private val _incomeUi = MutableStateFlow(IncomeTransactionUi())
    val incomeUi = _incomeUi.asStateFlow()

    private val _incomeUiEvent = MutableSharedFlow<IncomeUiEvent>()
    val incomeUiEvent = _incomeUiEvent.asSharedFlow()

    private val _getIncomeTransactionByDate =
        MutableStateFlow<List<IncomeTransactionUi>>(emptyList())
    val getIncomeTransactionByDate = _getIncomeTransactionByDate.asStateFlow()

    private val _selectedFilter = MutableStateFlow<DateFilterType>(DateFilterType.Today)
    val selectedFilter: StateFlow<DateFilterType> = _selectedFilter.asStateFlow()

    val incomeCategories = listOf(
        "Salary",
        "Freelance",
        "Investments",
        "Rental Income",
        "Gifts",
        "Business",
        "Interest",
        "Dividends",
        "Selling Assets",
        "Refunds",
        "Others"
    )

    fun onTabSelected(index: Int) {
        _incomeUiState.update {
            it.copy(selectedTab = index)
        }
    }

    fun calculatePercentageAmount(label: String, percentage: Double) {
        val transactions = incomeUiState.value.incomeTransactionList
        val total = transactions.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
        val amount = formatNumberToIndianStyle(total * percentage)

        _incomeUiState.update {
            when (label) {
                "Need" -> it.copy(needPercentageAmount = amount)
                "Want" -> it.copy(wantPercentageAmount = amount)
                "Invest" -> it.copy(investPercentageAmount = amount)
                else -> it
            }
        }
    }


    fun calculateTotalIncome() {
        val transactionList = incomeUiState.value.incomeTransactionList
        Log.d("TAG", "Transaction List Size: ${transactionList.size}")

        val totalIncome = transactionList.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
        Log.d("TAG", "Calculated Total Income: $totalIncome")

        _incomeUiState.update {
            Log.d("TAG", "Old State: $it")
            val newState = it.copy(totalIncome = formatNumberToIndianStyle(totalIncome))
            Log.d("TAG", "New State: $newState")
            newState
        }
    }


    fun calculateIncomeAverage() {
        val transactions = incomeUiState.value.incomeTransactionList
        if (transactions.isNotEmpty()) {
            val avg = transactions.sumOf { it.amount.toDoubleOrNull() ?: 0.0 } / transactions.size
            Log.d("TAG", "calculateIncomeAverage: $avg")
            _incomeUiState.update {
                it.copy(averageIncome = formatNumberToIndianStyle(avg))
            }
        }
    }

    fun clearForm() {
        _incomeUi.update {
            it.copy(
                amount = "",
                note = "",
                category = ""
            )
        }
    }

    fun clearAmount() {
        _incomeUi.update {
            it.copy(
                amount = ""
            )
        }
    }

    fun clearNote() {
        _incomeUi.update {
            it.copy(
                note = ""
            )
        }
    }


    fun updatedAmount(amount: String) {
        _incomeUi.update {
            it.copy(
                amount = amount
            )
        }
    }

    fun updatedNote(note: String) {
        _incomeUi.update {
            it.copy(
                note = note
            )
        }
    }

    fun setCategory(category: String) {
        _incomeUi.update {
            it.copy(
                category = category
            )
        }
    }


    fun addIncomeTransaction(incomeTransactionUi: IncomeTransactionUi) {
        viewModelScope.launch {

            _incomeUiEvent.emit(IncomeUiEvent.Loading)

            val result = incomeTransactionRepo.addIncomeTransaction(incomeTransactionUi)

            result.onSuccess {
                _incomeUiEvent.emit(IncomeUiEvent.Success("Add Transaction Successfully"))
            }.onFailure {
                _incomeUiEvent.emit(IncomeUiEvent.Error("Add Transaction Failed"))
            }
        }
    }

    init {
        getIncomeTransaction()
        getIncomeTransactionByDate(DateFilterType.Today)
    }

    fun onFilterSelected(filter: DateFilterType) {
        _selectedFilter.value = filter
        getIncomeTransactionByDate(filter)
    }

    fun getIncomeTransaction() {
        viewModelScope.launch {

            _incomeUiEvent.emit(IncomeUiEvent.Loading)

            incomeTransactionRepo.getIncomeTransaction().collectLatest { result ->
                result.onSuccess { newList ->
                    _incomeUiState.update {
                        it.copy(incomeTransactionList = newList)
                    }
                    calculateTotalIncome()
                    calculateIncomeAverage()
                    calculatePercentageAmount(
                        "Need",
                        0.5
                    )
                    calculatePercentageAmount(
                        "Want",
                        0.3
                    )
                    calculatePercentageAmount(
                        "Invest",
                        0.2
                    )

                    Log.d("TAG", "getIncomeTransaction: ${newList.size}")
                }.onFailure {
                    Log.d("TAG", "getIncomeTransaction: ${it.message}")
                    _incomeUiEvent.emit(IncomeUiEvent.Error("Get All Income Transaction Failed"))
                }

            }
        }
    }

    fun getIncomeTransactionByDate(dateFilterType: DateFilterType) {
        viewModelScope.launch {
            _incomeUiEvent.emit(IncomeUiEvent.Loading)
            incomeTransactionRepo.getIncomeTransactionByDate(dateFilterType).collectLatest { result ->
                    result.onSuccess { data ->
                        _getIncomeTransactionByDate.value = data
                        _incomeUiEvent.emit(IncomeUiEvent.Success("Income loaded"))
                        Log.d("TAG", "getIncomeTransactionByDate: $data")
                    }
                    result.onFailure { error ->
                        _incomeUiEvent.emit(IncomeUiEvent.Error(error.message ?: "Unknown error"))
                    }
                }
        }
    }

    fun editIncomeTransaction(incomeTransactionUi: IncomeTransactionUi) {
        viewModelScope.launch {
            _incomeUiEvent.emit(IncomeUiEvent.Loading)

            val result = incomeTransactionRepo.editIncomeTransaction(incomeTransactionUi)

            result.onSuccess {
                _incomeUiEvent.emit(IncomeUiEvent.Success("Edit Income Transaction Successfully"))
                Log.d("Edit", "Transaction updated successfully")
            }.onFailure {
                _incomeUiEvent.emit(IncomeUiEvent.Error("Edit Income Transaction Failed"))
                Log.e("Edit", "Error updating: ${it.message}")
            }
        }
    }

    fun deleteIncomeTransaction(id: String) {
        viewModelScope.launch {
            _incomeUiEvent.emit(IncomeUiEvent.Loading)

            val result = incomeTransactionRepo.deleteIncomeTransaction(id)

            result.onSuccess {
                _incomeUiEvent.emit(IncomeUiEvent.Success("Delete Income Transaction Successfully"))

                Log.d("Delete", "Transaction deleted successfully ")
            }.onFailure {
                _incomeUiEvent.emit(IncomeUiEvent.Error("Edit Income Transaction Failed"))
                Log.e("Delete", "Error updating: ${it.message}")
            }
        }
    }

}