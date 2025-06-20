package com.dipuguide.finslice.presentation.screens.addTransaction.income

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dipuguide.finslice.data.repo.IncomeTransactionRepo
import com.dipuguide.finslice.presentation.model.IncomeTransactionUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddIncomeViewModel @Inject constructor(
    private val incomeTransactionRepo: IncomeTransactionRepo,
) : ViewModel() {

    private val _addIncomeUiState = MutableStateFlow(IncomeTransactionUi())
    val addIncomeUiState = _addIncomeUiState.asStateFlow()

    private val _addIncomeUiEvent = MutableSharedFlow<AddIncomeUiEvent>()
    val addIncomeUiEvent = _addIncomeUiEvent.asSharedFlow()

    private val _selectedTab = MutableStateFlow<Int>(0)
    val selectedTab = _selectedTab.asStateFlow()


    val incomeCategories = listOf(
        "Salary",
        "Part-Time",
        "Rental Income",
        "Business Profit",
        "Stock Market Profit",
        "Mutual Funds",
        "Pension",
        "Government Subsidy",
        "Scholarship",
        "Cashback",
        "Other Income"
    )


    fun setDate(millis: Long) {
        _addIncomeUiState.update {
            it.copy(
                date = millis
            )
        }
    }

    fun onTabSelected(index: Int) {
        _selectedTab.value = index
    }


    fun clearForm() {
        _addIncomeUiState.update {
            it.copy(
                amount = "",
                note = "",
                category = ""
            )
        }
    }

    fun clearAmount() {
        _addIncomeUiState.update {
            it.copy(
                amount = ""
            )
        }
    }

    fun clearNote() {
        _addIncomeUiState.update {
            it.copy(
                note = ""
            )
        }
    }


    fun updatedAmount(amount: String) {
        _addIncomeUiState.update {
            it.copy(
                amount = amount
            )
        }
    }

    fun updatedNote(note: String) {
        _addIncomeUiState.update {
            it.copy(
                note = note
            )
        }
    }

    fun setCategory(category: String) {
        _addIncomeUiState.update {
            it.copy(
                category = category
            )
        }
    }

    fun addIncomeTransaction() {
        viewModelScope.launch {
            _addIncomeUiEvent.emit(AddIncomeUiEvent.Loading)
            val incomeTransactionUi = addIncomeUiState.value
            // ✅ Check input amount before saving (prevents crash or saving zero)
            val amount = incomeTransactionUi.amount.toDoubleOrNull()
            if (amount == null || amount <= 0) {
                _addIncomeUiEvent.emit(AddIncomeUiEvent.Error("Please enter a valid income amount greater than 0"))
                Log.w(
                    "AddIncomeViewModel",
                    "⚠️ Invalid amount input: ${incomeTransactionUi.amount}"
                )
                return@launch
            }

            // 🔄 Save transaction
            val result = incomeTransactionRepo.addIncomeTransaction(incomeTransactionUi)

            result.onSuccess {
                _addIncomeUiState.update { data ->
                    data.copy(
                        id = data.id,
                        amount = data.amount,
                        note = data.note,
                        category = data.category,
                        date = addIncomeUiState.value.date
                    )
                }
                _addIncomeUiEvent.emit(AddIncomeUiEvent.Success("Income added successfully"))
            }.onFailure {
                Log.e("AddIncomeViewModel", "❌ Failed to save income", it)
                _addIncomeUiEvent.emit(AddIncomeUiEvent.Error("Failed to save income. Please try again."))
            }
        }
    }

}