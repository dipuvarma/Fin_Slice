package com.dipuguide.finslice.presentation.screens.addTransaction.income

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dipuguide.finslice.domain.model.IncomeTransaction
import com.dipuguide.finslice.domain.repo.IncomeTransactionRepo
import com.dipuguide.finslice.presentation.common.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AddIncomeViewModel @Inject constructor(
    private val incomeTransactionRepo: IncomeTransactionRepo,
) : ViewModel() {


    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _addIncomeUiState = MutableStateFlow(AddIncomeUiState())
    val addIncomeUiState = _addIncomeUiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<AddIncomeNavigation>()
    val navigationEvent = _navigationEvent.asSharedFlow()


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


    fun onEvent(event: AddIncomeEvent) {
        when (event) {
            is AddIncomeEvent.AmountChange -> {
                _addIncomeUiState.update {
                    it.copy(
                        amount = event.amount
                    )
                }
            }

            is AddIncomeEvent.NoteChange -> {
                _addIncomeUiState.update {
                    it.copy(
                        note = event.note
                    )
                }
            }

            is AddIncomeEvent.CategoryChange -> {
                _addIncomeUiState.update {
                    it.copy(
                        category = event.category
                    )
                }
            }

            is AddIncomeEvent.DatePickerChange -> {
                _addIncomeUiState.update {
                    it.copy(
                        createdAt = event.date
                    )
                }
            }

            AddIncomeEvent.SaveClick -> {
                addIncomeTransaction(
                    IncomeTransaction(
                        amount = addIncomeUiState.value.amount.toDouble(),
                        note = addIncomeUiState.value.note,
                        category = addIncomeUiState.value.category,
                        createdAt = addIncomeUiState.value.createdAt
                    )
                )
            }
        }
    }


    fun onTabSelected(index: Int) {
        _selectedTab.value = index
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

     fun clearForm() {
        _addIncomeUiState.update {
            it.copy(
                amount = "",
                note = "",
                category = "",
            )
        }
    }

    private fun addIncomeTransaction(incomeTransaction: IncomeTransaction) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            val result = incomeTransactionRepo.addIncomeTransaction(incomeTransaction)

            result.onSuccess {
                _uiState.value = UiState.Success("Income added successfully")
                _navigationEvent.emit(AddIncomeNavigation.HOME)
            }.onFailure {
                Timber.e(it, "❌ Failed to save income")
                _uiState.value = UiState.Error("Failed to save income. Please try again.")
            }
        }
    }

    fun resetUiState() {
        _uiState.value = UiState.Idle
    }

}