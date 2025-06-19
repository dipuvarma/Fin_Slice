package com.dipuguide.finslice.presentation.screens.main.home

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dipuguide.finslice.R
import com.dipuguide.finslice.data.repo.DataStoreRepository
import com.dipuguide.finslice.presentation.component.BudgetCategoryCard
import com.dipuguide.finslice.presentation.component.CustomTopAppBar
import com.dipuguide.finslice.presentation.component.FormLabel
import com.dipuguide.finslice.presentation.component.MonthYearPickerDialog
import com.dipuguide.finslice.presentation.component.TransactionDashboard
import com.dipuguide.finslice.presentation.screens.auth.AuthViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    authViewModel: AuthViewModel,
    onOverViewClick: () -> Unit,
    innerPadding: PaddingValues,
) {
    val uiState by homeViewModel.homeUiState.collectAsState()
    val isRefreshing by homeViewModel.isRefreshing.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val selectedMonth by homeViewModel.selectedMonth.collectAsStateWithLifecycle()
    val selectedYear by homeViewModel.selectedYear.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }

    val userDetail by homeViewModel.userDetails.collectAsState()

    val getUserDetail by authViewModel.getUserDetails.collectAsState()

    // ✅ Removed unnecessary collectAsState of sharedFlow
    LaunchedEffect(Unit) {
        Log.d("userDetail", "userDetail: $getUserDetail")
        homeViewModel.homeUiEvent.collect { event ->
            when (event) {
                is HomeUiEvent.Loading -> {
                    Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show()

                }

                is HomeUiEvent.Success -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                is HomeUiEvent.Error -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                else -> Unit
            }
        }
    }

    val userName = userDetail.name.takeUnless { it.isNullOrEmpty() } // 1. Use userDetail.name if it's not null or empty
        ?: getUserDetail.name.takeUnless { it.isNullOrEmpty() } // 2. Otherwise, use getUserDetail.name if it's not null or empty
        ?: "Fin Slice" // 3. Otherwise, default to "Fin Slice"

    Column(
        modifier = Modifier
            .padding(innerPadding)
    ) {
        CustomTopAppBar(
            title = userName,
            image = userDetail.photo,
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
                currentMonth = selectedMonth,
                currentYear = selectedYear,
                onDismiss = { showDialog = false },
                onConfirm = { month, year ->
                    homeViewModel.setMonthAndYear(month = month, year = year)
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = { homeViewModel.refresh() }
        ) {
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
}






