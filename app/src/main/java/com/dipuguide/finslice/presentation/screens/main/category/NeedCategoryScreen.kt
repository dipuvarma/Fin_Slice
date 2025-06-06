package com.dipuguide.finslice.presentation.screens.main.category

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dipuguide.finslice.presentation.screens.main.transaction.ExpenseTransactionViewModel

@Composable
fun NeedCategoryScreen(
    expenseViewModel: ExpenseTransactionViewModel,
) {

    val uiState = expenseViewModel.getExpenseByCategory.collectAsState()

    LaunchedEffect(Unit) {
        expenseViewModel.getAllExpensesByCategory("Need")
    }

    LazyColumn {
        items(uiState.value.expenseTransactionList) {
            Log.d("TAG", "NeedCategoryScreen: $it")
        }
    }

}