package com.dipuguide.finslice.presentation.screens.addTransaction.expense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dipuguide.finslice.domain.model.ExpenseTransaction
import com.dipuguide.finslice.domain.repo.ExpenseTransactionRepo
import com.dipuguide.finslice.presentation.common.state.UiState
import com.dipuguide.finslice.presentation.screens.main.home.HomeUiState
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
class AddExpenseViewModel @Inject constructor(
    private val expenseTransactionRepo: ExpenseTransactionRepo,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _addExpenseUiState = MutableStateFlow(AddExpenseUiState())
    val addExpenseUiState = _addExpenseUiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<AddExpenseNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    val expenseCategories = listOf("Need", "Want", "Invest")

    val expenseTagsByCategory = mapOf(
        "Need" to listOf(
            "Groceries",
            "House Rent",
            "Electricity Bill",
            "Water Bill",
            "Gas Cylinder",
            "Mobile Recharge",
            "Medical",
            "Public Transport",
            "School/College Fees",
            "Loan EMI",
            "Insurance",
            "Maid Salary",
            "Others"
        ),

        "Want" to listOf(
            "Online Shopping",
            "Clothing & Fashion",
            "Dining Out",
            "Movies/OTT",
            "Travel & Holidays",
            "Gadgets",
            "Gym/Fitness",
            "Beauty & Salon",
            "Pet Care",
            "Hobbies",
            "Gaming & Apps",
            "Others"
        ),

        "Invest" to listOf(
            "Fixed Deposit",
            "Mutual Funds",
            "Stocks",
            "Gold",
            "Crypto",
            "PPF",
            "Real Estate",
            "Startup Investment",
            "Others"
        )
    )


    fun onEvent(event: AddExpenseEvent) {
        when (event) {
            is AddExpenseEvent.AmountChange -> {
                _addExpenseUiState.update {
                    it.copy(
                        amount = event.amount
                    )
                }
            }

            is AddExpenseEvent.NoteChange -> {
                _addExpenseUiState.update {
                    it.copy(
                        note = event.note
                    )
                }
            }

            is AddExpenseEvent.CategoryChange -> {
                _addExpenseUiState.update {
                    it.copy(
                        category = event.category
                    )
                }
            }

            is AddExpenseEvent.TagChange -> {
                _addExpenseUiState.update {
                    it.copy(
                        tag = event.tag
                    )
                }
            }

            is AddExpenseEvent.DatePickerClick -> {
                _addExpenseUiState.update {
                    it.copy(
                        createdAt = event.millis
                    )
                }
            }

            AddExpenseEvent.SaveExpenseClick -> {
                addExpenseTransaction(
                    ExpenseTransaction(
                        amount = addExpenseUiState.value.amount.toDouble(),
                        note = addExpenseUiState.value.note,
                        category = addExpenseUiState.value.category,
                        tag = addExpenseUiState.value.tag,
                        createdAt = addExpenseUiState.value.createdAt
                    )
                )
            }
        }
    }


    fun clearAmount() {
        _addExpenseUiState.update {
            it.copy(
                amount = ""
            )
        }
    }

    fun clearNote() {
        _addExpenseUiState.update {
            it.copy(
                note = ""
            )
        }
    }

    fun clearForm() {
        _addExpenseUiState.update {
            it.copy(
                amount = "",
                note = "",
                category = "",
                tag = ""
            )
        }
    }

    fun addExpenseTransaction(expenseTransaction: ExpenseTransaction) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            val result = expenseTransactionRepo.addExpenseTransaction(expenseTransaction)

            result.onSuccess {
                _uiState.value = UiState.Success("Expense added successfully")
                _navigationEvent.emit(AddExpenseNavigationEvent.MAIN)
                Timber.d("Add Expense Successfully")

            }
            result.onFailure {
                Timber.e(it, "Failed to add expense")
                _uiState.value = UiState.Error("Something went wrong while saving expense")
            }
        }
    }

    fun validateAmount(homeUiState: HomeUiState): String? {
        val inputAmount = addExpenseUiState.value.amount.toDoubleOrNull()
            ?: return "Please enter a valid amount"

        val totalIncome = homeUiState.totalIncome.replace(",", "").toDoubleOrNull() ?: 0.0

        if (inputAmount > totalIncome) {
            return "Your income is ₹${homeUiState.totalIncome}. You can't exceed this limit."
        }

        val category = addExpenseUiState.value.category
        when (category) {
            "Need" -> {
                val needLimit =
                    homeUiState.needPercentageAmount.replace(",", "").toDoubleOrNull() ?: 0.0
                val needSpent =
                    homeUiState.needExpenseTotal.replace(",", "").toDoubleOrNull() ?: 0.0
                if ((inputAmount + needSpent) >= needLimit) {
                    return "Need budget remaining: ₹${needLimit - needSpent}"
                }
            }

            "Want" -> {
                val wantSpent =
                    homeUiState.wantExpenseTotal.replace(",", "").toDoubleOrNull() ?: 0.0
                val wantLimit =
                    homeUiState.wantPercentageAmount.replace(",", "").toDoubleOrNull() ?: 0.0
                if ((inputAmount + wantSpent) >= wantLimit) {
                    return "Want budget remaining: ₹${wantLimit - wantSpent}"
                }
            }

            "Invest" -> {
                val investSpent =
                    homeUiState.investExpenseTotal.replace(",", "").toDoubleOrNull() ?: 0.0
                val investLimit =
                    homeUiState.investPercentageAmount.replace(",", "").toDoubleOrNull() ?: 0.0
                if ((inputAmount + investSpent) >= investLimit) {
                    return "Invest budget remaining: ₹${investLimit - investSpent}"
                }
            }
        }
        return null // No error
    }

    fun resetUiState() {
        _uiState.value = UiState.Idle
    }

}