package com.dipuguide.finslice.presentation.component
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dipuguide.finslice.R
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePicker(
    onDateSelected: (Long) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Select date",
    dateFormat: String = "EEE, dd MMM yyyy",
    icon: @Composable (() -> Unit)? = null
) {
    var showDialog by remember { mutableStateOf(false) }

    // Calculate today's date in UTC at midnight (your original logic)
    val todayMillis = remember {
        LocalDate.now(ZoneOffset.UTC)
            .atStartOfDay(ZoneOffset.UTC)
            .toInstant()
            .toEpochMilli()
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = todayMillis,
        initialDisplayedMonthMillis = todayMillis,
        initialDisplayMode = DisplayMode.Picker,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                // Disable future dates (your original logic)
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
                        datePickerState.selectedDateMillis?.let {
                            onDateSelected(it)
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

    // The date selector button (with your original styling)
    Button(
        onClick = { showDialog = true },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            contentColor = MaterialTheme.colorScheme.background,
            containerColor = MaterialTheme.colorScheme.onBackground
        ),
        shape = MaterialTheme.shapes.small
    ) {
        icon?.invoke() ?: Icon(
            painter = painterResource(id = R.drawable.calendar_icon),
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = datePickerState.selectedDateMillis?.let { millis ->
                val date = Instant.ofEpochMilli(millis)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                DateTimeFormatter.ofPattern(dateFormat).format(date)
            } ?: label,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}