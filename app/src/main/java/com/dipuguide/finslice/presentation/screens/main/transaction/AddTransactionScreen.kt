package com.dipuguide.finslice.presentation.screens.main.transaction

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dipuguide.finslice.presentation.screens.main.transaction.IncomeTransactionViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    viewModel: IncomeTransactionViewModel,
    navController: NavController
) {
    val uiState = viewModel.incomeUiState.collectAsState()
    val tabTitles = listOf("Expense", "Income")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        // Tab Row

        TabRow(
            selectedTabIndex = uiState.value.selectedTab,
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = uiState.value.selectedTab == index,
                    onClick = { viewModel.onTabSelected(index) },
                    text = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.labelLarge,
                            color = if (uiState.value.selectedTab == index)
                                MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onBackground
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp)) // optional padding

        // Show the selected screen
        when (uiState.value.selectedTab) {
            0 -> AddExpenseScreen()
            1 -> AddIncomeScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
    }
}