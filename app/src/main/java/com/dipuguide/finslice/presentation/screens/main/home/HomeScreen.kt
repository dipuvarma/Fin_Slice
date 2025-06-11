package com.dipuguide.finslice.presentation.screens.main.home

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dipuguide.finslice.presentation.component.BudgetCategoryCard
import com.dipuguide.finslice.presentation.component.CustomTopAppBar
import com.dipuguide.finslice.presentation.component.FormLabel
import com.dipuguide.finslice.presentation.component.TransactionDashboard
import com.dipuguide.finslice.presentation.screens.main.transaction.ExpenseTransactionViewModel
import com.dipuguide.finslice.presentation.screens.main.transaction.IncomeTransactionViewModel
import com.dipuguide.finslice.utils.formatNumberToIndianStyle


@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    onOverViewClick: () -> Unit,
    innerPadding: PaddingValues,
) {
    val uiState by homeViewModel.homeUiState.collectAsState()

    // ✅ Removed unnecessary collectAsState of sharedFlow
    LaunchedEffect(Unit) {
        homeViewModel.homeUiEvent.collect { event ->
            when (event) {
                is HomeUiEvent.Loading -> {
                    // TODO: Show loading dialog/spinner
                }

                is HomeUiEvent.Success -> {
                    // TODO: Snackbar or toast
                }

                is HomeUiEvent.Error -> {
                    // TODO: Show error message
                }

                else -> Unit
            }
        }
    }

    Column(
        modifier = Modifier.padding(innerPadding)
    ) {
        CustomTopAppBar(title = "Dipu")
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            item {
                TransactionDashboard(
                    onOverViewClick = onOverViewClick,
                    netBalanceAmount = uiState.netBalance,
                    expenseAmount = uiState.totalExpense,
                    incomeAmount = uiState.totalIncome
                )
                Column(modifier = Modifier.padding(16.dp)) {
                    FormLabel(text = "CATEGORY")
                    Spacer(modifier = Modifier.height(8.dp))
                    BudgetCategoryCard(
                        title = "Need",
                        spentAmount = uiState.needExpenseTotal,
                        totalAmount = uiState.needPercentageAmount,
                        color = MaterialTheme.colorScheme.primary
                    )
                    BudgetCategoryCard(
                        title = "Want",
                        spentAmount = uiState.wantExpenseTotal,
                        totalAmount = uiState.wantPercentageAmount,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    BudgetCategoryCard(
                        title = "Invest",
                        spentAmount = uiState.investExpenseTotal,
                        totalAmount = uiState.investPercentageAmount,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}




