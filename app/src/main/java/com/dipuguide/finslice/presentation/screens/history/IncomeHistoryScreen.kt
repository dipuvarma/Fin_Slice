package com.dipuguide.finslice.presentation.screens.history

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
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
import com.dipuguide.finslice.presentation.component.TransactionCardComp
import com.dipuguide.finslice.presentation.screens.main.transaction.ExpenseTransactionUiEvent
import com.dipuguide.finslice.presentation.screens.main.transaction.IncomeTransactionViewModel
import com.dipuguide.finslice.presentation.screens.main.transaction.IncomeUiEvent
import com.dipuguide.finslice.utils.DateFilterType
import kotlinx.coroutines.flow.collectLatest

@Composable
fun IncomeHistoryScreen(
    incomeViewModel: IncomeTransactionViewModel,
) {


    val getAllIncomeByDate by incomeViewModel.getIncomeTransactionByDate.collectAsStateWithLifecycle()
    val selectedFilter by incomeViewModel.selectedFilter.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(true) {
        incomeViewModel.incomeUiEvent.collectLatest { event ->
            when (event) {
                is IncomeUiEvent.Loading -> {
                    Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show()
                }

                is IncomeUiEvent.Success -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                is IncomeUiEvent.Error -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        }
    }

    val filters = mapOf(
        "Today" to DateFilterType.Today,
        "Yesterday" to DateFilterType.Yesterday,
        "Last 7 Days" to DateFilterType.Last7Days,
        "This Month" to DateFilterType.ThisMonth,
        "This Year" to DateFilterType.ThisYear
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
    ) {
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            ) {
                item {
                    filters.forEach { (label, filterType) ->
                        FilterChip(
                            modifier = Modifier.padding(end = 8.dp),
                            selected = (selectedFilter == filterType),
                            onClick = {
                                incomeViewModel.onFilterSelected(filterType)
                            },
                            label = { Text(label) },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = if (selectedFilter == filterType) {
                                    MaterialTheme.colorScheme.onBackground
                                } else {
                                    Color.Transparent
                                },
                                labelColor = if (selectedFilter == filterType) {
                                    MaterialTheme.colorScheme.background
                                } else {
                                    MaterialTheme.colorScheme.onBackground
                                }
                            )
                        )
                    }
                }
            }
        }
        items(getAllIncomeByDate, key = { it.id!! }) { income ->
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = .5f)
            )
            TransactionCardComp(
                category = income.category,
                categoryMatch = "Income",
                note = income.note,
                amount = income.amount,
                date = income.date,
                onDeleteClick = {},
                onEditClick = {}
            )
        }
    }
}