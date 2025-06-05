package com.dipuguide.finslice.presentation.screens.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dipuguide.finslice.presentation.screens.main.transaction.IncomeTransactionViewModel

@Composable
fun IncomeHistoryScreen(
    viewModel: IncomeTransactionViewModel,
) {

    val textColor = MaterialTheme.colorScheme.onSurface
    val uiState by viewModel.incomeUiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getIncomeTransaction()
    }
    LazyColumn {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Category",
                    style = MaterialTheme.typography.titleMedium,
                    color = textColor,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Amount",
                    style = MaterialTheme.typography.titleMedium,
                    color = textColor,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
        items(uiState.incomeTransactionList) { income ->
            Surface(
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 2.dp,
                shadowElevation = 2.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column() {
                        Text(
                            text = income.category,
                            style = MaterialTheme.typography.titleMedium,
                            color = textColor
                        )
                        if (!income.note.isNullOrEmpty()) {
                            Text(
                                text = income.note,
                                style = MaterialTheme.typography.bodyMedium,
                                color = textColor.copy(alpha = .5f)
                            )
                        }
                    }
                    Text(
                        text = "₹ ${income.amount}",
                        style = MaterialTheme.typography.titleMedium,
                        color = textColor
                    )
                }
            }
        }
    }

}