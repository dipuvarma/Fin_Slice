package com.dipuguide.finslice.presentation.component

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dipuguide.finslice.R

@Composable
fun TotalAmountCardComp(
    amountType: String,
    totalAmount: String,
    modifier: Modifier = Modifier
) {
    val total = remember(totalAmount) {
        totalAmount
            .replace("[^\\d]".toRegex(), "")
            .toIntOrNull()
            ?.takeIf { it >= 0 }
            ?: 0
    }

    val animatedTotal by animateIntAsState(
        targetValue = total,
        animationSpec = tween(durationMillis = 1200),
        label = "AnimatedTotal"
    )

    val formattedTotal = "₹ %,d".format(animatedTotal)

    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        shape = MaterialTheme.shapes.small
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Icon(
                painter = if (amountType == "EXPENSE") painterResource(R.drawable.dollar_down_icon)
                else painterResource(R.drawable.dollar_up_icon),
                contentDescription = amountType,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .background(
                        if (amountType == "INCOME") MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.errorContainer,
                        shape = CircleShape
                    )
                    .padding(6.dp)
                    .size(20.dp)
            )

            Column {
                Text(
                    text = amountType,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(.8f)
                    ),
                    modifier = Modifier.padding(end = 24.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formattedTotal,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(end = 24.dp)
                )
            }
        }
    }
}
