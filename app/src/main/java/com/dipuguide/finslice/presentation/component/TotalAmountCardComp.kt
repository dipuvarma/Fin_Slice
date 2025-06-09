package com.dipuguide.finslice.presentation.component

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun TotalAmountCardComp(
    amountType: String,
    totalAmount: String,
    icon: ImageVector,
    tintBgColor: Color,
    iconColor: Color,
) {

    val total = totalAmount.replace(",", "").toIntOrNull() ?: 1 // prevent divide by 0

    val animatedTotal by animateIntAsState(
        targetValue = total,
        animationSpec = tween(durationMillis = 1200),
        label = "AnimatedTotal"
    )
    val formattedTotal = "₹ %,d".format(animatedTotal)

    Surface(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .width(170.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        shape = MaterialTheme.shapes.small
    ) {
        Box(
            modifier = Modifier.padding(16.dp)
        ) {
            // Top-right icon
            Icon(
                imageVector = icon,
                contentDescription = amountType,
                tint = tintBgColor,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .background(iconColor, shape = CircleShape)
                    .padding(6.dp)
                    .size(20.dp)
            )

            Column {
                Text(
                    text = amountType,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(.8f),
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formattedTotal,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                    )
                )
            }
        }
    }
}