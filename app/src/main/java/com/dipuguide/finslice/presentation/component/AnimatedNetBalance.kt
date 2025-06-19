package com.dipuguide.finslice.presentation.component

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedNetBalance(
    balance: String,
) {
    val targetValue = remember(balance) {
        balance.replace("[^\\d.]".toRegex(), "").toFloatOrNull() ?: 0f
    }

    // Animate the numeric value
    val animatedValue by animateFloatAsState(
        targetValue = targetValue,
        animationSpec = tween(durationMillis = 1000),
        label = "countingAnimation"
    )

    // Format with ₹ symbol
    val displayValue = remember(animatedValue) {
        "₹ ${"%,.2f".format(animatedValue)}" // Formats with commas and 2 decimal places
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "NET AMOUNT",
            style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(.8f),
                letterSpacing = 2.sp
            )
        )
        Spacer(modifier = Modifier.height(4.dp))

        // Keep your existing AnimatedContent for the transition
        Text(
            text = displayValue,
            style = MaterialTheme.typography.headlineMedium.copy(
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
        )

    }
}