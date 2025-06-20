package com.dipuguide.finslice.presentation.screens.main.history

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.dipuguide.finslice.presentation.component.TransactionCardComp
import com.dipuguide.finslice.presentation.navigation.AddTransaction
import com.dipuguide.finslice.utils.DateFilterType
import com.dipuguide.finslice.utils.formatTimestampToDateTime
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.flow.collectLatest


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseHistoryScreen(
    historyViewModel: TransactionHistoryViewModel,
) {

    val getAllExpenseByDate by historyViewModel.getAllExpenseByDate.collectAsStateWithLifecycle()
    val selectedFilter by historyViewModel.selectedFilter.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val isRefreshing by historyViewModel.isRefreshing.collectAsStateWithLifecycle()


    val filters = mapOf(
        "Today" to DateFilterType.Today,
        "Yesterday" to DateFilterType.Yesterday,
        "Last 7 Days" to DateFilterType.Last7Days,
        "This Month" to DateFilterType.ThisMonth,
        "This Year" to DateFilterType.ThisYear
    )
    // 🔁 SWIPE TO REFRESH WRAPPER
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = {
            historyViewModel.refresh()
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
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
                                    historyViewModel.onFilterSelected(filterType)
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

            items(getAllExpenseByDate, key = { it.id!! }) { expense ->
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outline.copy(alpha = .5f)
                )
                TransactionCardComp(
                    category = expense.category,
                    categoryMatch = "Expense",
                    note = expense.note,
                    amount = expense.amount,
                    tag = expense.tag,
                    date = formatTimestampToDateTime(expense.date!!),
                    onDeleteClick = {
                        historyViewModel.deleteExpenseTransaction(id = expense.id!!)
                    },
                    onEditClick = {

                    }
                )
            }
        }
    }

}


