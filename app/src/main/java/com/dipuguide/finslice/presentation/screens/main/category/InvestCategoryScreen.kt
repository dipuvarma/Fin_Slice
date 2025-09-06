package com.dipuguide.finslice.presentation.screens.main.category

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dipuguide.finslice.presentation.common.component.ExpenseTransactionCardComp
import com.dipuguide.finslice.presentation.common.state.UiState
import com.dipuguide.finslice.utils.formatTimestampToDateTime
import kotlinx.coroutines.flow.collectLatest

@Composable
fun InvestCategoryScreen(
    viewModel: CategoryViewModel,
) {
    val categoryUiState by viewModel.categoryUiState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current


    LaunchedEffect(Unit) {
        viewModel.loadCategoryExpenses()
    }

    LaunchedEffect(uiState) {
        when (uiState) {
            is UiState.Success -> {
                val successMessage = (uiState as UiState.Success).message
                Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show()
                viewModel.resetUiState()
            }

            is UiState.Error -> {
                val errorMessage = (uiState as UiState.Error).error
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                viewModel.resetUiState()
            }

            else -> Unit
        }
    }

    LazyColumn {
        items(categoryUiState.expenseInvestList) { expense ->
            HorizontalDivider()
            ExpenseTransactionCardComp(
                amount = expense.amount.toString(),
                category = expense.category,
                note = expense.note,
                tag = expense.tag,
                date = formatTimestampToDateTime(expense.createdAt),
                onEditClick = {
                    /*TODO*/
                },
                onDeleteClick = {
                    viewModel.onEvent(CategoryEvent.DeleteClick(expense.id))
                }
            )
        }
    }
}