package com.dipuguide.finslice.presentation.common.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.util.Calendar

@Composable
fun MonthYearPicker(
    selectedMonth: Int,
    selectedYear: Int,
    onDateSelected: (month: Int, year: Int) -> Unit,
) {
    val months = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )
    val currentYear = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        LocalDate.now().year
    } else {
        Calendar.getInstance().get(Calendar.YEAR)
    }
    val yearRange = (currentYear - 5)..(currentYear + 5)

    var expandedMonth by remember { mutableStateOf(false) }
    var expandedYear by remember { mutableStateOf(false) }

    Column {
        Row {
            // Month Dropdown
            Box {
                Row (
                    modifier = Modifier.background(
                        MaterialTheme.colorScheme.onBackground,
                        MaterialTheme.shapes.small,
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    TextButton(
                        onClick = { expandedMonth = true },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.background,
                        ),
                    ) {
                        Text(months[selectedMonth - 1])
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = if (!expandedMonth) Icons.Default.ArrowDropDown else Icons.Default.ArrowDropUp,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.background,
                    )
                }
                DropdownMenu(
                    expanded = expandedMonth,
                    onDismissRequest = { expandedMonth = false }
                ) {
                    months.forEachIndexed { index, monthName ->
                        DropdownMenuItem(
                            text = { Text(monthName) },
                            onClick = {
                                expandedMonth = false
                                onDateSelected(index + 1, selectedYear)
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.width(16.dp))

            // Year Dropdown
            Box {
                Row(
                    modifier = Modifier.background(
                        MaterialTheme.colorScheme.onBackground,
                        MaterialTheme.shapes.small,
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = { expandedYear = true },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.background,
                        ),
                    ) {
                        Text("$selectedYear")
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = if (!expandedYear) Icons.Default.ArrowDropDown else Icons.Default.ArrowDropUp,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.background,
                    )
                }
                DropdownMenu(
                    expanded = expandedYear,
                    onDismissRequest = { expandedYear = false }
                ) {
                    yearRange.forEach { year ->
                        DropdownMenuItem(
                            text = { Text("$year") },
                            onClick = {
                                expandedYear = false
                                onDateSelected(selectedMonth, year)
                            }
                        )
                    }
                }
            }
        }
    }
}
