package com.dipuguide.finslice.presentation.screens.addTransaction

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dipuguide.finslice.presentation.component.TopAppBarComp
import com.dipuguide.finslice.presentation.navigation.Main
import com.dipuguide.finslice.presentation.screens.addTransaction.expense.AddExpenseScreen
import com.dipuguide.finslice.presentation.screens.addTransaction.expense.AddExpenseViewModel
import com.dipuguide.finslice.presentation.screens.addTransaction.income.AddIncomeScreen
import com.dipuguide.finslice.presentation.screens.addTransaction.income.AddIncomeViewModel
import com.dipuguide.finslice.presentation.screens.main.home.HomeViewModel
import com.dipuguide.finslice.presentation.screens.main.transaction.IncomeTransactionViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    addIncomeViewModel: AddIncomeViewModel,
    addExpenseViewModel: AddExpenseViewModel,
    homeViewModel: HomeViewModel,
    navController: NavController,
) {
    val tabTitles = listOf("Expense", "Income")

    val selectedTab by addIncomeViewModel.selectedTab.collectAsState()


    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        TopAppBarComp(
            title = "Add Transaction",
            onClickNavigationIcon = {
                navController.navigate(Main){
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            },
            navigationIcon = Icons.Default.ArrowBack
        )
        // 🔸 Custom TabRow
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
                        .clickable { addIncomeViewModel.onTabSelected(index) }
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
        Spacer(modifier = Modifier.height(16.dp)) // optional padding
        // Show the selected screen
        Box(modifier = Modifier.weight(1f)) {
            when (selectedTab) {
                0 -> AddExpenseScreen(
                    addExpenseViewModel = addExpenseViewModel,
                    homeViewModel = homeViewModel,
                    navController = navController
                )

                1 -> AddIncomeScreen(
                    addIncomeViewModel = addIncomeViewModel,
                    navController = navController
                )
            }
        }
    }
}