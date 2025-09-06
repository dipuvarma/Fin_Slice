package com.dipuguide.finslice.presentation.screens.addTransaction

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dipuguide.finslice.R
import com.dipuguide.finslice.presentation.common.component.TopAppBarComp
import com.dipuguide.finslice.presentation.navigation.MainRoute
import com.dipuguide.finslice.presentation.screens.addTransaction.expense.AddExpenseScreen
import com.dipuguide.finslice.presentation.screens.addTransaction.expense.AddExpenseViewModel
import com.dipuguide.finslice.presentation.screens.addTransaction.income.AddIncomeScreen
import com.dipuguide.finslice.presentation.screens.addTransaction.income.AddIncomeViewModel
import com.dipuguide.finslice.presentation.screens.main.home.HomeViewModel

data class TabItem(val label: String, val index: Int)

@Composable
fun AddTransactionScreen(
    addIncomeViewModel: AddIncomeViewModel,
    addExpenseViewModel: AddExpenseViewModel,
    homeViewModel: HomeViewModel,
    navController: NavController,
) {

    val selectedTab by addIncomeViewModel.selectedTab.collectAsState()

    val tabItems = listOf(
        TabItem(label = stringResource(id = R.string.tab_expense), index = 0),
        TabItem(label = stringResource(id = R.string.tab_income), index = 1)
    )

    val onTabSelected = remember {
        { index: Int ->
            addIncomeViewModel.onTabSelected(index)
        }
    }

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        TopAppBarComp(
            title = "Add Transaction",
            onClickNavigationIcon = {
                navController.navigate(MainRoute) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            },
            navigationIcon = Icons.AutoMirrored.Filled.ArrowBack
        )
        // Custom TabRow
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .height(40.dp)
        ) {
            tabItems.forEachIndexed { index, text ->
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
                        .clickable { onTabSelected(index) }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = text.label,
                        style = MaterialTheme.typography.bodyMedium,
                        color = contentColor
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Show the selected screen
        Box(modifier = Modifier.weight(1f)) {
            when (selectedTab) {
                0 -> AddExpenseScreen(
                    viewModel = addExpenseViewModel,
                    homeViewModel = homeViewModel,
                    navController = navController
                )
                1 -> AddIncomeScreen(
                    viewModel = addIncomeViewModel,
                    navController = navController
                )
            }
        }
    }
}


