package com.dipuguide.finslice.presentation.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthYearPickerDialog(
    currentMonth: Int,
    currentYear: Int,
    onDismiss: () -> Unit,
    onConfirm: (month: Int, year: Int) -> Unit
) {
    val months = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    val years = (2024..LocalDate.now().year + 5).toList()

    var selectedMonthIndex by remember { mutableIntStateOf(currentMonth - 1) }
    var selectedYear by remember { mutableIntStateOf(currentYear) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Month and Year") },
        text = {
            Column {
                // Month dropdown
                DropdownMenuWithLabel(
                    label = "Month",
                    items = months,
                    selectedIndex = selectedMonthIndex,
                    onItemSelected = { selectedMonthIndex = it }
                )

                Spacer(Modifier.height(16.dp))

                // Year dropdown
                DropdownMenuWithLabel(
                    label = "Year",
                    items = years.map { it.toString() },
                    selectedIndex = years.indexOf(selectedYear),
                    onItemSelected = { selectedYear = years[it] }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirm(selectedMonthIndex + 1, selectedYear)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun DropdownMenuWithLabel(
    label: String,
    items: List<String>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = items.getOrNull(selectedIndex) ?: "",
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            }
        )

        // Transparent clickable area on top
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Transparent)
                .clickable { expanded = true }
        )
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier.fillMaxWidth()
    ) {
        items.forEachIndexed { index, item ->
            DropdownMenuItem(
                onClick = {
                    onItemSelected(index)
                    expanded = false
                },
                text = {
                    Text(item)
                }
            )
        }
    }
}

