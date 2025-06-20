package com.dipuguide.finslice.presentation.screens.main.category

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dipuguide.finslice.data.repo.ExpenseTransactionRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val expenseTransactionRepo: ExpenseTransactionRepo,
) : ViewModel() {

    companion object {
        private const val TAG = "CategoryViewModel"
    }

    private val _categoryUiState = MutableStateFlow(CategoryUiState())
    val categoryUiState: StateFlow<CategoryUiState> = _categoryUiState.asStateFlow()

    private val _categoryUiEvent = MutableSharedFlow<CategoryUiEvent>()
    val categoryUiEvent: SharedFlow<CategoryUiEvent> = _categoryUiEvent.asSharedFlow()

    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    init {
        loadCategoryExpenses()
    }

    /**
     * Update selected tab index
     */
    fun onSelectedTab(index: Int) {
        Log.d(TAG, "Tab selected: $index")
        _selectedTab.value = index
    }

    /**
     * Load all expenses for fixed categories
     */
    private fun loadCategoryExpenses() {
        listOf("Need", "Want", "Invest").forEach { category ->
            getAllExpensesByCategory(category)
        }
    }

    /**
     * Fetch expenses by category and update UI state
     */
    fun getAllExpensesByCategory(category: String) {
        viewModelScope.launch {
            Log.d(TAG, "Fetching expenses for category: $category")
            _categoryUiEvent.emit(CategoryUiEvent.Loading)

            expenseTransactionRepo.getAllExpensesByCategory(category)
                .distinctUntilChanged()
                .collectLatest { result ->

                    result.onSuccess { expenses ->
                        Log.d(
                            TAG,
                            "Fetched ${expenses.size} expenses for category: $category"
                        )

                        _categoryUiState.update { currentState ->
                            when (category) {
                                "Need" -> currentState.copy(expenseNeedList = expenses)
                                "Want" -> currentState.copy(expenseWantList = expenses)
                                "Invest" -> currentState.copy(expenseInvestList = expenses)
                                else -> currentState
                            }
                        }

//                        _categoryUiEvent.emit(
//                            CategoryUiEvent.Success("Expenses loaded successfully for category: $category")
//                        )
                    }

                    result.onFailure { exception ->
                        Log.e(
                            TAG,
                            "Failed to fetch expenses for category: $category",
                            exception
                        )
                        _categoryUiEvent.emit(
                            CategoryUiEvent.Error("Failed to load $category expenses")
                        )
                    }
                }
        }
    }


    fun deleteExpenseTransaction(id: String) {
        viewModelScope.launch {
            val result = expenseTransactionRepo.deleteExpenseTransaction(id = id)

            result.onSuccess {
                _categoryUiEvent.emit(CategoryUiEvent.Success("Delete Successfully"))
            }

            result.onFailure {
                _categoryUiEvent.emit(CategoryUiEvent.Error("Delete Failed"))
            }
        }
    }
}
