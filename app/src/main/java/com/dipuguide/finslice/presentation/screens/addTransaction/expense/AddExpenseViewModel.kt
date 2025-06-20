package com.dipuguide.finslice.presentation.screens.addTransaction.expense

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dipuguide.finslice.data.repo.ExpenseTransactionRepo
import com.dipuguide.finslice.presentation.screens.main.home.HomeUiState
import com.dipuguide.finslice.presentation.model.ExpenseTransactionUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddExpenseViewModel @Inject constructor(
    private val expenseTransactionRepo: ExpenseTransactionRepo,
) : ViewModel() {

    private val _addExpenseUiState = MutableStateFlow(ExpenseTransactionUi())
    val addExpenseUiState = _addExpenseUiState.asStateFlow()

    private val _addExpenseUiEvent = MutableSharedFlow<AddExpenseUiEvent>()
    val addExpenseUiEvent = _addExpenseUiEvent.asSharedFlow()

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


    fun setDate(millis: Long) {
        _addExpenseUiState.update {
            it.copy(
                date = millis
            )
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

    fun setCategory(category: String) {
        _addExpenseUiState.update {
            it.copy(
                category = category
            )
        }
    }

    fun setTag(tag: String) {
        _addExpenseUiState.update {
            it.copy(
                tag = tag
            )
        }
    }

    fun updatedAmount(amount: String) {
        _addExpenseUiState.update {
            it.copy(
                amount = amount
            )
        }
    }

    fun updatedNote(note: String) {
        _addExpenseUiState.update {
            it.copy(
                note = note
            )
        }
    }

    fun addExpenseTransaction() {
        viewModelScope.launch {
            _addExpenseUiEvent.emit(AddExpenseUiEvent.Loading)
            val expenseTransactionUi = addExpenseUiState.value
            // 🚫 If amount is empty or invalid, don't call repo
            val amountDouble = expenseTransactionUi.amount.toDoubleOrNull()
            if (amountDouble == null || amountDouble < 0) {
                _addExpenseUiEvent.emit(AddExpenseUiEvent.Error("Please enter a valid amount greater than 0"))
                Log.w(
                    "addExpenseTransaction",
                    "⚠️ Invalid amount input: ${expenseTransactionUi.amount}"
                )
                return@launch
            }

            val result = expenseTransactionRepo.addExpenseTransaction(expenseTransactionUi)
            result.onSuccess {
                _addExpenseUiState.update { data ->
                    ExpenseTransactionUi(
                        id = data.id,
                        amount = data.amount,
                        note = data.note,
                        category = data.category,
                        tag = data.tag,
                        date = addExpenseUiState.value.date
                    )
                }
                Log.d("addExpenseTransaction ", "Add Expense Successfully")
                _addExpenseUiEvent.emit(AddExpenseUiEvent.Success("Expense added successfully"))
            }
            result.onFailure {
                Log.e("addExpenseTransaction", "❌ Failed to add expense", it)
                _addExpenseUiEvent.emit(AddExpenseUiEvent.Error("Something went wrong while saving expense"))
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

}