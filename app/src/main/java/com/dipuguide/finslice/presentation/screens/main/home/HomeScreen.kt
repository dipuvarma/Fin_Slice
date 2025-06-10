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
    incomeViewModel: IncomeTransactionViewModel,
    expenseViewModel: ExpenseTransactionViewModel,
    onOverViewClick: () -> Unit,
    innerPadding: PaddingValues,
) {

    val incomeUiState by incomeViewModel.incomeUiState.collectAsState()
    val expenseUiState by expenseViewModel.allExpenseUiState.collectAsState()
    val expenseByCategory by expenseViewModel.getExpenseByCategory.collectAsState()

    val incomeAmount = incomeUiState.totalIncome
    val expenseAmount = expenseUiState.totalExpense

    val incomeAmountRaw = incomeUiState.totalIncome.replace(",", "").toDoubleOrNull() ?: 0.0
    val expenseAmountRaw = expenseUiState.totalExpense.replace(",", "").toDoubleOrNull() ?: 0.0

    Log.d("netBalance", "Income: ${incomeUiState.totalIncome}")
    Log.d("netBalance", "Expense: ${expenseUiState.totalExpense}")

    val netBalance = incomeAmountRaw - expenseAmountRaw
    val formattedNetBalance = formatNumberToIndianStyle(netBalance)

    Log.d("netBalance", "Net: $netBalance")
    Log.d("netBalance", "Formatted: $formattedNetBalance")


    Column(
        modifier = Modifier
            .padding(innerPadding)
    ) {
        CustomTopAppBar(
            title = "Dipu",
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            item {
                TransactionDashboard(
                    onOverViewClick = {
                        onOverViewClick()
                    },
                    netBalanceAmount = formattedNetBalance,
                    expenseAmount = expenseAmount,
                    incomeAmount = incomeAmount
                )
                Column(modifier = Modifier.padding(16.dp)) {
                    FormLabel(text = "CATEGORY")
                    Spacer(modifier = Modifier.height(8.dp))
                    BudgetCategoryCard(
                        title = "Need",
                        spentAmount = expenseByCategory.needExpenseAmount,
                        totalAmount = incomeUiState.needPercentageAmount,
                        color = MaterialTheme.colorScheme.primary
                    )
                    BudgetCategoryCard(
                        title = "Want",
                        spentAmount = expenseByCategory.wantExpenseAmount,
                        totalAmount = incomeUiState.wantPercentageAmount,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    BudgetCategoryCard(
                        title = "Invest",
                        spentAmount = expenseByCategory.investExpenseAmount,
                        totalAmount = incomeUiState.investPercentageAmount,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}



