package com.dipuguide.finslice.presentation.screens.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.dipuguide.finslice.presentation.component.TransactionCardComp
import com.dipuguide.finslice.presentation.screens.main.transaction.IncomeTransactionViewModel

@Composable
fun IncomeHistoryScreen(
    incomeViewModel: IncomeTransactionViewModel,
) {
    val uiState by incomeViewModel.incomeUiState.collectAsState()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
    ) {
        items(uiState.incomeTransactionList, key = { it.id!! }) { income ->
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = .5f)
            )
            TransactionCardComp(
                category = income.category,
                note = income.note,
                amount = income.amount,
                date = income.date,
                icon = Icons.Default.ArrowUpward,
                iconColor = Color(0xFFD0F0DA),
                iconBgColor = Color(0xFF2E7D32),
                amountColor = Color(0xFF2E7D32)
            )
        }
    }
}