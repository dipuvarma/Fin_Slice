package com.dipuguide.finslice.presentation.screens.main.transaction

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dipuguide.finslice.data.repo.IncomeTransactionRepo
import com.dipuguide.finslice.utils.DateFilterType
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