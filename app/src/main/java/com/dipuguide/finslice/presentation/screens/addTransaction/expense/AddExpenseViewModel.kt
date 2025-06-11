package com.dipuguide.finslice.presentation.screens.addTransaction.expense

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dipuguide.finslice.data.repo.ExpenseTransactionRepo
import com.dipuguide.finslice.data.repo.IncomeTransactionRepo
import com.dipuguide.finslice.presentation.screens.main.transaction.ExpenseTransactionUi
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
            "Food",
            "Rent",
            "Utilities",
            "Transport",
            "Medical",
            "Groceries",
            "Insurance",
            "Phone Bill",
            "Education",
            "Childcare"
        ),
        "Want" to listOf(
            "Shopping",
            "Entertainment",
            "Dining Out",
            "Streaming",
            "Travel",
            "Gadgets",
            "Subscriptions",
            "Gaming",
            "Hobbies",
            "Fitness Classes"
        ),
        "Invest" to listOf(
            "Stocks",
            "Mutual Funds",
            "Crypto",
            "Real Estate",
            "Gold",
            "Bonds",
            "Pension Fund",
            "REITs",
            "Startup Investment",
            "SIP (Systematic Investment Plan)"
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
                _addExpenseUiEvent.emit(AddExpenseUiEvent.Success("Add Expense Successfully"))
            }
            result.onFailure {
                Log.e("addExpenseTransaction ", "Add Expense Failed", it)
                _addExpenseUiEvent.emit(AddExpenseUiEvent.Error("Add Expense Failed"))
            }
        }
    }

}