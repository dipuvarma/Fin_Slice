package com.dipuguide.finslice.presentation.screens.main.home

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dipuguide.finslice.presentation.component.AnimatedNetBalance
import com.dipuguide.finslice.presentation.component.BudgetCategoryCard
import com.dipuguide.finslice.presentation.component.TopAppBarComp
import com.dipuguide.finslice.presentation.component.TransactionInfoCard
import com.dipuguide.finslice.presentation.navigation.Report
import com.dipuguide.finslice.presentation.screens.auth.AuthViewModel
import com.dipuguide.finslice.presentation.screens.main.transaction.ExpenseTransactionUiEvent
import com.dipuguide.finslice.presentation.screens.main.transaction.ExpenseTransactionViewModel
import com.dipuguide.finslice.presentation.screens.main.transaction.IncomeTransactionViewModel
import com.dipuguide.finslice.presentation.screens.main.transaction.IncomeUiEvent
import com.dipuguide.finslice.utils.formatNumberToIndianStyle


@Composable
fun HomeScreen(
    incomeViewModel: IncomeTransactionViewModel,
    expenseViewModel: ExpenseTransactionViewModel,
    onOverViewClick:() -> Unit
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



    Column() {
        TopAppBarComp(
            title = "Fin Slice"
        )

        TransactionDashboard(
            onOverViewClick = {
                onOverViewClick()
            },
            netBalanceAmount = formattedNetBalance,
            expenseAmount = expenseAmount,
            incomeAmount = incomeAmount
        )
        Column(modifier = Modifier.padding(16.dp)) {

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


@Composable
fun TransactionDashboard(
    onOverViewClick: () -> Unit,
    netBalanceAmount: String,
    expenseAmount: String,
    incomeAmount: String,
) {
    val gradient = Brush.linearGradient(
        listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.secondary
        )
    )
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = MaterialTheme.shapes.large,
        tonalElevation = 2.dp,
        shadowElevation = 4.dp,
        color = Color.Transparent // Needed for gradient
    ) {
        Box(
            modifier = Modifier
                .background(brush = gradient, shape = MaterialTheme.shapes.large)
                .padding(20.dp)
        ) {
            TransactionDashboardContent(
                onOverViewClick = onOverViewClick,
                netBalanceAmount = netBalanceAmount,
                expenseAmount = expenseAmount,
                incomeAmount = incomeAmount
            )
        }
    }
}


@Composable
fun TransactionDashboardContent(
    onOverViewClick: () -> Unit,
    netBalanceAmount: String,
    expenseAmount: String,
    incomeAmount: String,

    ) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        //Monthly Overview
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Monthly Overview",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
            )
            IconButton(
                onClick = { onOverViewClick() },
                colors = IconButtonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.primary,
                    disabledContentColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "See more",
                )
            }
        }

        // NetBalance
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            AnimatedNetBalance(
                balance = netBalanceAmount
            )
        }

        //Transaction
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TransactionInfoCard(
                icon = Icons.Default.TrendingDown,
                iconColor = MaterialTheme.colorScheme.error,
                label = "Expense",
                amount = expenseAmount
            )
            TransactionInfoCard(
                icon = Icons.Default.TrendingUp,
                iconColor = MaterialTheme.colorScheme.tertiary,
                label = "Income",
                amount = incomeAmount
            )
        }
    }
}











