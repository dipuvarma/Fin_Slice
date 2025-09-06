package com.dipuguide.finslice.presentation.screens.main.history

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dipuguide.finslice.presentation.common.component.TopAppBarComp

@Composable
fun TransactionHistoryScreen(
    historyViewModel: TransactionHistoryViewModel,
    innerPadding: PaddingValues,
) {


    val selectedTab by historyViewModel.selectedTab.collectAsState()
    val tabTitles = listOf("Expense", "Income")

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TopAppBarComp(title = "All Transaction")

            Row(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(horizontal = 16.dp)
                    .height(40.dp)
            ) {
                tabTitles.forEachIndexed { index, text ->
                    val selected = selectedTab == index
                    val backgroundColor =
                        if (selected) MaterialTheme.colorScheme.onBackground else Color.Transparent
                    val contentColor =
                        if (selected) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onSurface

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(MaterialTheme.shapes.small)
                            .background(backgroundColor)
                            .clickable { historyViewModel.onTabSelected(index) }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyMedium,
                            color = contentColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                when (selectedTab) {
                    0 -> ExpenseHistoryScreen(
                        viewModel = historyViewModel,
                    )
                    1 -> IncomeHistoryScreen(historyViewModel)
                }
            }
        }
    }
}

