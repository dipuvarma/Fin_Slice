package com.dipuguide.finslice.presentation.screens.main.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dipuguide.finslice.R
import com.dipuguide.finslice.presentation.common.component.BudgetCategoryCard
import com.dipuguide.finslice.presentation.common.component.CustomTopAppBar
import com.dipuguide.finslice.presentation.common.component.FormLabel
import com.dipuguide.finslice.presentation.common.component.MonthYearPickerDialog
import com.dipuguide.finslice.presentation.common.component.TransactionDashboard

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    onOverViewClick:() -> Unit,
    innerPadding: PaddingValues,
) {
    val uiState by homeViewModel.uiState.collectAsState()
    val homeUiState by homeViewModel.homeUiState.collectAsState()
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    val userDetail by homeViewModel.userDetail.collectAsState()


    LaunchedEffect(Unit) {
        homeViewModel.homeNavigation.collect { navigation ->
            when (navigation) {
                HomeNavigation.REPORT -> {
                    onOverViewClick()
                }
            }
        }
    }


    Column(
        modifier = Modifier
            .padding(innerPadding)
    ) {
        CustomTopAppBar(
            title = userDetail.name,
            image = userDetail.photoUri,
            actions = {
                IconButton(onClick = {
                    showDialog = true
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.calendar_icon),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        )
        if (showDialog) {
            MonthYearPickerDialog(
                currentMonth = homeUiState.selectedMonth,
                currentYear = homeUiState.selectedYear,
                onDismiss = { showDialog = false },
                onConfirm = { month, year ->
                    homeViewModel.onEvent(HomeUiEvent.MonthPickerClick(month = month, year = year))
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            item {
                TransactionDashboard(
                    onOverViewClick = onOverViewClick,
                    netBalanceAmount = homeUiState.netAmount,
                    expenseAmount = homeUiState.totalExpense,
                    incomeAmount = homeUiState.totalIncome
                )
                Column(modifier = Modifier.padding(16.dp)) {
                    FormLabel(text = "CATEGORY")
                    Spacer(modifier = Modifier.height(8.dp))
                    BudgetCategoryCard(
                        title = "Need",
                        spentAmount = homeUiState.needExpenseTotal,
                        totalAmount = homeUiState.needPercentageAmount,
                        color = MaterialTheme.colorScheme.primary
                    )
                    BudgetCategoryCard(
                        title = "Want",
                        spentAmount = homeUiState.wantExpenseTotal,
                        totalAmount = homeUiState.wantPercentageAmount,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    BudgetCategoryCard(
                        title = "Invest",
                        spentAmount = homeUiState.investExpenseTotal,
                        totalAmount = homeUiState.investPercentageAmount,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }

    }
}






