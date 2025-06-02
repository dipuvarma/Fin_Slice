package com.dipuguide.finslice.presentation.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight

@Composable
fun FormLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.SemiBold
        )
    )
}