package com.dipuguide.finslice.presentation.screens.main

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dipuguide.finslice.data.model.Transaction
import com.dipuguide.finslice.presentation.component.AnimatedNetBalance
import com.dipuguide.finslice.presentation.component.BudgetCategoryCard
import com.dipuguide.finslice.presentation.component.TransactionInfoCard
import com.dipuguide.finslice.presentation.navigation.AddTransaction
import com.dipuguide.finslice.presentation.navigation.SignIn
import com.dipuguide.finslice.presentation.screens.auth.AuthViewModel
import com.dipuguide.finslice.presentation.screens.main.transaction.IncomeTransactionViewModel

@Composable
fun HomeScreen(
    viewModel: AuthViewModel,
    incomeViewModel: IncomeTransactionViewModel,
    navController: NavController,
) {
    Column {
        TransactionDashboard()
        Column(modifier = Modifier.padding(16.dp)) {
            BudgetCategoryCard(
                title = "Need",
                spentAmount = "2000",
                totalAmount = "10000",
                color = MaterialTheme.colorScheme.primary
            )
            BudgetCategoryCard(
                title = "Want",
                spentAmount = "1000",
                totalAmount = "70000",
                color = MaterialTheme.colorScheme.tertiary
            )
            BudgetCategoryCard(
                title = "Invest",
                spentAmount = "1500",
                totalAmount = "30000",
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionDashboard(modifier: Modifier = Modifier) {
    val gradient = Brush.linearGradient(
        listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.secondary
        )
    )
    Surface(
        modifier = modifier
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
            TransactionDashboardContent()
        }
    }
}



@Composable
fun TransactionDashboardContent() {
    Column (
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
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
                onClick = { /* Action */ },
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
                balance = "14,320"
            )
        }
        //Transaction
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            TransactionInfoCard(
                icon = Icons.Default.TrendingDown,
                iconColor = MaterialTheme.colorScheme.error,
                label = "Expense",
                amount = "1,000"
            )
            TransactionInfoCard(
                icon = Icons.Default.TrendingUp,
                iconColor = MaterialTheme.colorScheme.tertiary,
                label = "Income",
                amount = "10,300"
            )
        }
    }

}











