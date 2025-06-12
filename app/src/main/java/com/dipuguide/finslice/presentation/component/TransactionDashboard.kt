package com.dipuguide.finslice.presentation.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun TransactionDashboard(
    onOverViewClick: () -> Unit,
    netBalanceAmount: String,
    expenseAmount: String,
    incomeAmount: String,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        //Monthly Overview
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "MONTHLY OVERVIEW",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                )
            )
            IconButton(
                onClick = { onOverViewClick() },
                colors = IconButtonColors(
                    containerColor = MaterialTheme.colorScheme.onBackground,
                    contentColor = MaterialTheme.colorScheme.background,
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
        Spacer(modifier = Modifier.height(16.dp))

        TransactionDashboardContent(
            netBalanceAmount = netBalanceAmount,
            expenseAmount = expenseAmount,
            incomeAmount = incomeAmount
        )

        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TotalAmountCardComp(
                amountType = "EXPENSE",
                totalAmount = expenseAmount,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            TotalAmountCardComp(
                amountType = "INCOME",
                totalAmount = incomeAmount,
                modifier = Modifier.weight(1f)
            )
        }
    }
}


@Composable
fun TransactionDashboardContent(
    netBalanceAmount: String,
    expenseAmount: String,
    incomeAmount: String,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp, horizontal = 16.dp)
            ) {
                // NetBalance
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    AnimatedNetBalance(
                        balance = netBalanceAmount
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                SpendingProgressCard(
                    title = "SPENDING PROGRESS",
                    spentAmount = expenseAmount,
                    totalAmount = incomeAmount,
                    color = Color(0xFF2E7D32)
                )
            }

        }
    }
}


@Composable
fun SpendingProgressCard(
    title: String,
    spentAmount: String,
    totalAmount: String,
    color: Color,
) {
    val spent = spentAmount.replace(",", "").toIntOrNull() ?: 0
    val total = totalAmount.replace(",", "").toIntOrNull() ?: 1 // prevent divide by 0

    val targetProgress = spent.toFloat() / total.toFloat()

    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 600),
        label = "AnimatedProgress"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = .8f)
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Animated Progress Bar
        LinearProgressIndicator(
            progress = animatedProgress,
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp),
            color = color,
            trackColor = Color(0xFFD0F0DA)
        )
    }
}