package com.dipuguide.finslice.presentation.screens.main.category

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dipuguide.finslice.presentation.component.ExpenseTransactionCardComp
import com.dipuguide.finslice.utils.formatTimestampToDateTime
import kotlinx.coroutines.flow.collectLatest

@Composable
fun WantCategoryScreen(
    categoryViewModel: CategoryViewModel,
) {
    val uiState by categoryViewModel.categoryUiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(true) {
        categoryViewModel.categoryUiEvent.collectLatest { event ->
            when (event) {
                is CategoryUiEvent.Loading -> {
                    Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show()
                }

                is CategoryUiEvent.Success -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                is CategoryUiEvent.Error -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        }
    }
    LazyColumn {
        items(uiState.expenseWantList) {expense->
            Log.d("TAG", "NeedCategoryScreen: $expense")
            HorizontalDivider()
            ExpenseTransactionCardComp(
                amount = expense.amount,
                category = expense.category,
                note = expense.note,
                tag = expense.tag,
                date = formatTimestampToDateTime(expense.date!!),
                onEditClick = {},
                onDeleteClick = {
                    categoryViewModel.deleteExpenseTransaction(expense.id!!)
                }
            )
        }
    }
}