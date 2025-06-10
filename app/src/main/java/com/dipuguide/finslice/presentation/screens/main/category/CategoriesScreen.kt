package com.dipuguide.finslice.presentation.screens.main.category

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dipuguide.finslice.presentation.component.TopAppBarComp
import com.dipuguide.finslice.presentation.screens.main.transaction.ExpenseTransactionViewModel

@Composable
fun CategoriesScreen(
    expenseViewModel: ExpenseTransactionViewModel,
    innerPadding: PaddingValues
) {

    var selectedTab by remember {
        mutableIntStateOf(0)
    }

    val categoryListItem = listOf(
        "Need", "Want", "Invest"
    )
    val containerColor = MaterialTheme.colorScheme.surface
    val borderColor = MaterialTheme.colorScheme.primary
    val selectedColor = MaterialTheme.colorScheme.primaryContainer
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBarComp(
            title = "Categories",
        )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                categoryListItem.forEachIndexed { index, title ->
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
                            .clickable { selectedTab = index }
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
            when (selectedTab) {
                0 -> NeedCategoryScreen(expenseViewModel)
                1 -> WantCategoryScreen(expenseViewModel)
                2 -> InvestCategoryScreen(expenseViewModel)
            }

    }
}