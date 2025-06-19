package com.dipuguide.finslice.presentation.screens.main.report


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dipuguide.finslice.presentation.component.MonthYearPicker
import com.dipuguide.finslice.presentation.component.TopAppBarComp
import com.dipuguide.finslice.utils.toPercentageString
import ir.ehsannarmani.compose_charts.PieChart
import ir.ehsannarmani.compose_charts.models.Pie
import kotlin.math.absoluteValue

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReportScreen(
    innerPadding: PaddingValues,
    reportViewModel: ReportViewModel = viewModel(),
) {
    var selectedLabel by rememberSaveable { mutableStateOf("No tag selected") }
    var selectedAmount by rememberSaveable { mutableDoubleStateOf(0.0) }

    val allExpense by reportViewModel.allExpenseByMonth.collectAsState()
    val uiState by reportViewModel.reportUiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(rememberScrollState())
    ) {
        TopAppBarComp(title = "Transaction Report")
        ResponsiveBox(alignment = Alignment.CenterEnd) {
            MonthYearPicker(
                selectedMonth = uiState.selectedMonth,
                selectedYear = uiState.selectedYear,
                onDateSelected = { month, year ->
                    reportViewModel.onMonthYearSelected(month, year)
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        ResponsiveBox(alignment = Alignment.Center) {
            if (allExpense.isNotEmpty()) {
                val pieEntries = allExpense
                    .groupBy { it.tag ?: "Untagged" }
                    .map { (tag, expenses) ->
                        val totalAmount = expenses.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
                        Pie(
                            label = tag,
                            data = totalAmount,
                            color = getColorForIndex(tag.hashCode()),
                        )
                    }

                AddCustomPieChart(
                    pie = pieEntries,
                    onClickLabel = { selectedLabel = it },
                    onClickAmount = { selectedAmount = it }
                )
            } else {
                EmptyStateText(text = "No expenses for selected period")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (allExpense.isNotEmpty()){
            LabelAndValue(label = "Tag", value = selectedLabel)
            LabelAndValue(label = "Amount", value = "₹%.2f".format(selectedAmount))
            Spacer(modifier = Modifier.height(24.dp))
        }

        SectionTitle(
            title = "Expense History",
        )
        Spacer(modifier = Modifier.height(8.dp))
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Tag",
                    modifier = Modifier.fillMaxWidth(.3f),
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.primary
                    )
                )
                Text(
                    text = "Amount",
                    modifier = Modifier.fillMaxWidth(.3f),
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.primary
                    )
                )
                Text(
                    text = "Percentage",
                    modifier = Modifier.fillMaxWidth(.3f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            allExpense.groupBy { it.tag ?: "Untagged" }
                .forEach { (tag, expenses) ->
                    val amount = expenses.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
                    val percentage =
                        if (uiState.totalExpense > 0) (amount / uiState.totalExpense) * 100.0 else 0.0
                    ExpenseRow(tag = tag, amount = amount, percentage = percentage)
                }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (allExpense.isNotEmpty()){
            // 📌 Total Summary
            ResponsiveBox(
                alignment = Alignment.CenterEnd
            ) {
                LabelAndValue(
                    label = "Total Expense",
                    value = "₹%.2f".format(uiState.totalExpense),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        brush = Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.onBackground
                            )
                        )
                    )
                )
            }
        }

    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title.uppercase(),
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 16.dp),
        style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            brush = Brush.linearGradient(
                listOf(
                    MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.onBackground
                )
            )
        )
    )
}

@Composable
fun LabelAndValue(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    style: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyMedium,
) {
    Text(
        text = "$label: $value",
        style = style,
        modifier = modifier.padding(horizontal = 16.dp)
    )
}

@Composable
fun ExpenseRow(tag: String, amount: Double, percentage: Double) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            tag,
            modifier = Modifier.fillMaxWidth(.3f),
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = "₹%.2f".format(amount),
            modifier = Modifier.fillMaxWidth(.3f),
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = percentage.toPercentageString(),
            modifier = Modifier.fillMaxWidth(.3f),
            style = MaterialTheme.typography.bodyMedium
        )
    }

}

@Composable
fun EmptyStateText(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(16.dp),
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
fun ResponsiveBox(
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        contentAlignment = alignment
    ) {
        content()
    }
}


@Composable
fun getColorForIndex(index: Int): Color {
    // 🎨 Handpicked bright, high-contrast, vibrant colors
    val colors = listOf(
        Color(0xFFEF5350), // Red
        Color(0xFFAB47BC), // Purple
        Color(0xFF5C6BC0), // Indigo
        Color(0xFF29B6F6), // Blue
        Color(0xFF66BB6A), // Green
        Color(0xFFFFCA28), // Amber
        Color(0xFFFF7043), // Deep Orange
        Color(0xFF8D6E63), // Brown
        Color(0xFFA1887F), // Taupe
        Color(0xFF26A69A), // Teal
        Color(0xFFD4E157), // Lime
        Color(0xFFEC407A), // Pink
        Color(0xFF7E57C2), // Deep Purple
        Color(0xFF42A5F5), // Sky Blue
        Color(0xFFFFA726), // Orange
        Color(0xFF9CCC65), // Light Green
        Color(0xFF26C6DA), // Cyan
        Color(0xFFBDBDBD), // Grey
        Color(0xFF00ACC1), // Dark Cyan
        Color(0xFFE91E63)  // Hot Pink
    )

    return colors[index.absoluteValue % colors.size]
}




@Composable
fun AddCustomPieChart(
    pie: List<Pie>,
    onClickLabel: (String) -> Unit,
    onClickAmount: (Double) -> Unit,
) {
    var data by remember { mutableStateOf(pie) }

    PieChart(
        modifier = Modifier.size(200.dp),
        data = pie,
        onPieClick = {
            onClickLabel(it.label ?: "")
            onClickAmount(it.data)
            val pieIndex = data.indexOf(it)
            data = data.mapIndexed { index, pie ->
                pie.copy(selected = index == pieIndex)
            }
        },
        selectedScale = 1.2f,
        scaleAnimEnterSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        colorAnimEnterSpec = tween(300),
        colorAnimExitSpec = tween(300),
        scaleAnimExitSpec = tween(300),
        spaceDegreeAnimExitSpec = tween(300),
        style = Pie.Style.Fill
    )
}


