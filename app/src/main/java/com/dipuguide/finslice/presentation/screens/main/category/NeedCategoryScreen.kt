package com.dipuguide.finslice.presentation.screens.main.category

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.dipuguide.finslice.presentation.component.ExpenseTransactionCardComp
import com.dipuguide.finslice.presentation.screens.main.transaction.ExpenseTransactionViewModel
import kotlin.math.exp

@Composable
fun NeedCategoryScreen(
    expenseViewModel: ExpenseTransactionViewModel,
) {

    val uiState = expenseViewModel.getExpenseByCategory.collectAsState()

    LaunchedEffect(Unit) {
        expenseViewModel.getAllExpensesByCategory("Need")
    }

    LazyColumn {
        items(uiState.value.expenseTransactionList) {expense->
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