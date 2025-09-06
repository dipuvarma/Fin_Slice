package com.dipuguide.finslice.presentation.screens.main.category

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dipuguide.finslice.domain.repo.ExpenseTransactionRepo
import com.dipuguide.finslice.presentation.common.state.UiState
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

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _categoryUiState = MutableStateFlow(CategoryUiState())
    val categoryUiState: StateFlow<CategoryUiState> = _categoryUiState.asStateFlow()


    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()


    fun onSelectedTab(index: Int) {
        _selectedTab.value = index
    }


    fun loadCategoryExpenses() {
        listOf("Need", "Want", "Invest").forEach { category ->
            getAllExpensesByCategory(category)
        }
    }


    fun onEvent(event: CategoryEvent) {
        when (event) {
            is CategoryEvent.DeleteClick -> {
                deleteExpenseTransaction(event.expenseId)
            }

            is CategoryEvent.EditClick -> {

            }
        }
    }

    private fun getAllExpensesByCategory(category: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            expenseTransactionRepo.getAllExpensesByCategory(category)
                .distinctUntilChanged()
                .collectLatest { result ->
                    result.onSuccess { expenses ->
                        _categoryUiState.update { currentState ->
                            when (category) {
                                "Need" -> currentState.copy(expenseNeedList = expenses)
                                "Want" -> currentState.copy(expenseWantList = expenses)
                                "Invest" -> currentState.copy(expenseInvestList = expenses)
                                else -> currentState
                            }
                        }
                        _uiState.value = UiState.Success("Fetch")
                        _uiState.value = UiState.Idle
                    }

                    result.onFailure { exception ->
                        _uiState.value = UiState.Error("Failed to load $category expenses")
                        _uiState.value = UiState.Idle
                    }
                }
        }
    }


    fun deleteExpenseTransaction(expenseId: String) {
        viewModelScope.launch {
            val result = expenseTransactionRepo.deleteExpenseTransaction(expenseId = expenseId)
            result.onSuccess {
                _uiState.value = UiState.Success("Delete Successfully")
            }
            result.onFailure {
                _uiState.value = UiState.Error("Delete Failed")
            }
        }
    }

    fun resetUiState() {
        _uiState.value = UiState.Idle
    }

}
