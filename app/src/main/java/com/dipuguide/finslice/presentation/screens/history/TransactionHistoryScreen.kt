package com.dipuguide.finslice.presentation.screens.history

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dipuguide.finslice.presentation.component.CustomTopAppBar
import com.dipuguide.finslice.presentation.component.TopAppBarComp
import com.dipuguide.finslice.presentation.screens.main.transaction.AddExpenseScreen
import com.dipuguide.finslice.presentation.screens.main.transaction.AddIncomeScreen
import com.dipuguide.finslice.presentation.screens.main.transaction.ExpenseTransactionViewModel
import com.dipuguide.finslice.presentation.screens.main.transaction.IncomeTransactionViewModel

@Composable
fun TransactionHistoryScreen(
    historyViewModel: TransactionHistoryViewModel,
    incomeViewModel: IncomeTransactionViewModel,
    expenseViewModel: ExpenseTransactionViewModel,
) {

    val tabTitles = listOf("Expense", "Income")
    val selectedTab = historyViewModel.selectedTab

    val containerColor = MaterialTheme.colorScheme.surface
    val borderColor = MaterialTheme.colorScheme.primary
    val selectedColor = MaterialTheme.colorScheme.primaryContainer


    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(containerColor),
    ) {
        Column {
            TopAppBarComp(
                title = "All Transaction",
            )
            // 🔸 Custom Animated TabRow
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                tabTitles.forEachIndexed { index, title ->
                    val isSelected = selectedTab == index
                    val animatedBgColor by animateColorAsState(
                        targetValue = if (isSelected) selectedColor else Color.Transparent,
                        animationSpec = tween(300),
                        label = "TabBgColor"
                    )
                    val animatedPadding by animateDpAsState(
                        targetValue = if (isSelected) 20.dp else 16.dp,
                        animationSpec = tween(300),
                        label = "TabPadding"
                    )

                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        border = BorderStroke(1.5.dp, borderColor),
                        color = animatedBgColor,
                        tonalElevation = if (isSelected) 2.dp else 0.dp,
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.medium)
                            .clickable { historyViewModel.onTabSelected(index)}
                    ) {
                        Text(
                            text = title,
                            color = borderColor,
                            modifier = Modifier.padding(
                                horizontal = animatedPadding,
                                vertical = 10.dp
                            ),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))
            when (historyViewModel.selectedTab) {
                0 -> ExpenseHistoryScreen(expenseViewModel)
                1 -> IncomeHistoryScreen(
                    incomeViewModel,
                    historyViewModel
                )
            }
        }
    }
}
