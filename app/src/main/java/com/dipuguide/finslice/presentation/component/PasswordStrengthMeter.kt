package com.dipuguide.finslice.presentation.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dipuguide.finslice.utils.PasswordStrength

@Composable
fun PasswordStrengthMeter(strength: PasswordStrength) {
    val animatedProgress by animateFloatAsState(
        targetValue = strength.progress,
        animationSpec = tween(500),
        label = "strengthProgress"
    )

    val animatedColor by animateColorAsState(
        targetValue = strength.color,
        animationSpec = tween(500),
        label = "strengthColor"
    )

    Column {
        LinearProgressIndicator(
            progress = animatedProgress,
            color = animatedColor,
            trackColor = Color.LightGray,
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(4.dp))
        )
        Text(
            text = "Strength: ${strength.label}",
            color = animatedColor,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}