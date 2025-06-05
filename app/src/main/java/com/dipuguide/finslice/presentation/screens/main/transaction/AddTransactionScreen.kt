package com.dipuguide.finslice.presentation.screens.main.transaction

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
    val selectedTab = uiState.value.selectedTab

    val borderColor = MaterialTheme.colorScheme.primary
    val selectedColor = MaterialTheme.colorScheme.primaryContainer

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        // 🔸 Custom Animated TabRow
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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
                        .clickable { viewModel.onTabSelected(index) }
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
        when (uiState.value.selectedTab) {
            0 -> AddExpenseScreen()
            1 -> AddIncomeScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
    }
}