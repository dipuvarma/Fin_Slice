package com.dipuguide.finslice.presentation.screens.main.category

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dipuguide.finslice.presentation.component.ExpenseTransactionCardComp
import com.dipuguide.finslice.presentation.screens.main.transaction.ExpenseTransactionUiEvent
import com.dipuguide.finslice.presentation.screens.main.transaction.ExpenseTransactionViewModel
import com.dipuguide.finslice.utils.DateFilterType
import kotlinx.coroutines.flow.collectLatest
import kotlin.collections.component1
import kotlin.collections.component2

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
                date = expense.date,
                onEditClick = {},
                onDeleteClick = {}
            )
        }
    }
}