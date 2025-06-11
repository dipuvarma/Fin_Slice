package com.dipuguide.finslice.presentation.screens.addTransaction

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.dipuguide.finslice.presentation.screens.main.transaction.IncomeTransactionViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    addIncomeViewModel: AddIncomeViewModel,
    addExpenseViewModel: AddExpenseViewModel,
    navController: NavController,
) {
    val tabTitles = listOf("Expense", "Income")

    val selectedTab by addIncomeViewModel.selectedTab.collectAsState()

    val borderColor = MaterialTheme.colorScheme.primary
    val selectedColor = MaterialTheme.colorScheme.primaryContainer

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopAppBarComp(
            title = "Add Transaction",
            onClickNavigationIcon = {
                navController.navigate(Main)
            },
            navigationIcon = Icons.Default.ArrowBack
        )
        // 🔸 Custom Animated TabRow
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            tabTitles.forEachIndexed { index, title ->
                Log.d("TAG", "TransactionScreen: $index $title")
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
                        .clickable { addIncomeViewModel.onTabSelected(index) }
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
        Spacer(modifier = Modifier.height(16.dp)) // optional padding
        // Show the selected screen
        Box(modifier = Modifier.weight(1f)) {
            when (selectedTab) {
                0 -> AddExpenseScreen(
                    addExpenseViewModel = addExpenseViewModel,
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