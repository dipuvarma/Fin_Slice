package com.dipuguide.finslice.presentation.screens.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dipuguide.finslice.data.model.IncomeTransaction
import com.dipuguide.finslice.data.repo.IncomeTransactionRepo
import com.dipuguide.finslice.data.repo.TransactionRepository
import com.dipuguide.finslice.presentation.mapper.toIncomeTransaction
import com.dipuguide.finslice.presentation.mapper.toIncomeTransactionUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IncomeTransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val incomeTransactionRepo: IncomeTransactionRepo,
) : ViewModel() {

    private val _getIncomeUiState = MutableStateFlow(IncomeTransactionUiState())
    val getIncomeUiState = _getIncomeUiState.asStateFlow()

    private val _incomeUiState = MutableStateFlow(IncomeTransactionUi())
    val incomeUiState = _incomeUiState.asStateFlow()

    private val _incomeUiEvent = MutableSharedFlow<IncomeUiEvent>()
    val incomeUiEvent = _incomeUiEvent.asSharedFlow()


    fun updatedAmount(amount: String) {
        _incomeUiState.update {
            it.copy(
                amount = amount
            )
        }
    }

    fun updatedNote(note: String) {
        _incomeUiState.update {
            it.copy(
                note = note
            )
        }
    }

    fun updatedCategory(category: String) {
        _incomeUiState.update {
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

    fun getIncomeTransaction() {
        viewModelScope.launch {

            _incomeUiEvent.emit(IncomeUiEvent.Loading)

            incomeTransactionRepo.getIncomeTransaction().collectLatest { listIncome ->
                listIncome.onSuccess {
                    _getIncomeUiState.value = getIncomeUiState.value.copy(
                        incomeTransactionList = it
                    )
                    _incomeUiEvent.emit(IncomeUiEvent.Success("Get All Income Transaction Successfully"))
                    Log.d("TAG", "getIncomeTransaction: ${it.size}")
                }.onFailure {
                    Log.d("TAG", "getIncomeTransaction: ${it.message}")
                    _incomeUiEvent.emit(IncomeUiEvent.Error("Get All Income Transaction Failed"))
                }

            }
        }
    }

    init {
      deleteIncomeTransaction(
          id = "Xzkb1G9Ue2ZXm6HxBaxL"
      )
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
