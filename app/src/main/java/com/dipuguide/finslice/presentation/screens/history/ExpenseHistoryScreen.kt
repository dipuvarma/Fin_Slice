package com.dipuguide.finslice.presentation.screens.history

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dipuguide.finslice.presentation.component.TransactionCardComp
import com.dipuguide.finslice.presentation.screens.main.transaction.ExpenseTransactionViewModel
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale


@Suppress("AutoBoxing")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseHistoryScreen(
    expenseViewModel: ExpenseTransactionViewModel,
    historyViewModel: TransactionHistoryViewModel,
) {

    val allUiState by expenseViewModel.allExpenseUiState.collectAsState()

    val selectedDate by historyViewModel.selectedDate.collectAsState()

    val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    var showDialog by remember { mutableStateOf(false) }


    val todayMillis = remember {
        LocalDate.now(ZoneOffset.UTC)  // Force UTC date
            .atStartOfDay(ZoneOffset.UTC) // Midnight UTC
            .toInstant()
            .toEpochMilli()
    }
    Log.d("todayMillis", "ExpenseHistoryScreen: $todayMillis")
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = todayMillis,
        initialDisplayedMonthMillis = todayMillis,
        initialDisplayMode = DisplayMode.Picker,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                // Example: Disable future dates
                return utcTimeMillis <= todayMillis
            }
        },
        yearRange = 1900..LocalDate.now().year
    )

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            historyViewModel.setDate(millis)
                        }
                        showDialog = false
                    },
                    enabled = datePickerState.selectedDateMillis != null
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                dateFormatter = remember { DatePickerDefaults.dateFormatter() }
            )
        }
    }



    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
    ) {
        item {
            Surface(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = datePickerState.selectedDateMillis?.let { millis ->
                            val date = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                            "Selected: ${DateTimeFormatter.ofPattern("dd MMM yyyy").format(date)}"
                        } ?: "No date selected",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Button(onClick = { showDialog = true }) {
                        Text("Pick Today Only")
                    }
                }
            }
        }
        items(allUiState.expenseTransactionList, key = { it.id!! }) { expense ->
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = .5f)
            )
            TransactionCardComp(
                category = expense.category,
                categoryMatch = "Expense",
                note = expense.note,
                amount = expense.amount,
                tag = expense.tag,
                date = expense.date,
                onDeleteClick = {},
                onEditClick = {}
            )
        }
    }
}


