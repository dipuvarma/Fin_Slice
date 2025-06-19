package com.dipuguide.finslice.presentation.screens.main.category

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.traceEventStart
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dipuguide.finslice.presentation.component.TopAppBarComp
import com.dipuguide.finslice.presentation.screens.main.history.TransactionHistoryViewModel
import com.dipuguide.finslice.presentation.screens.main.transaction.ExpenseTransactionViewModel
import com.dipuguide.finslice.utils.DateFilterType
import kotlin.collections.component1
import kotlin.collections.component2

@Composable
fun CategoriesScreen(
    categoryViewModel: CategoryViewModel,
    innerPadding: PaddingValues,
) {

   // val uiState by categoryViewModel.categoryUiState.collectAsStateWithLifecycle()
    // val selectedCategory by expenseViewModel.selectedCategory.collectAsStateWithLifecycle()
    // val selectedFilter by expenseViewModel.selectedFilter.collectAsStateWithLifecycle()
    // val context = LocalContext.current

    val selectedTab by categoryViewModel.selectedTab.collectAsState(0)

    val categoryListItem = listOf(
        "Need", "Want", "Invest"
    )

    val filters = mapOf(
        "Today" to DateFilterType.Today,
        "Yesterday" to DateFilterType.Yesterday,
        "Last 7 Days" to DateFilterType.Last7Days,
        "This Month" to DateFilterType.ThisMonth,
        "This Year" to DateFilterType.ThisYear
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        TopAppBarComp(
            title = "Categories",
        )
        Row(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = MaterialTheme.shapes.small
                )
                .padding(horizontal = 16.dp)
                .height(40.dp)
        ) {
            categoryListItem.forEachIndexed { index, text ->
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
                        .clickable {
                            categoryViewModel.onSelectedTab(index)
                        }
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
        Spacer(modifier = Modifier.height(16.dp))
        when (selectedTab) {
            0 -> NeedCategoryScreen(
               categoryViewModel =  categoryViewModel
            )
            1 -> WantCategoryScreen(
               categoryViewModel =  categoryViewModel
            )
            2 -> InvestCategoryScreen(
                categoryViewModel = categoryViewModel
            )
        }
    }
}