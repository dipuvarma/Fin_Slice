package com.dipuguide.finslice.presentation.screens.main.report

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dipuguide.finslice.presentation.component.TopAppBarComp

@Composable
fun ReportScreen(modifier: Modifier = Modifier, innerPadding: PaddingValues) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBarComp(
            title = "Transaction Report"
        )
    }
}