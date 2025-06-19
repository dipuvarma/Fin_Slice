package com.dipuguide.finslice.presentation.screens.auth.onBoard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dipuguide.finslice.R
import com.dipuguide.finslice.presentation.navigation.GettingStart
import com.dipuguide.finslice.presentation.navigation.Main
import com.dipuguide.finslice.presentation.screens.auth.AuthViewModel
import com.dipuguide.finslice.utils.Destination

@Composable
fun SplashScreen(
    authViewModel: AuthViewModel,
    navController: NavController,
) {
    // Handle Navigation Events
    LaunchedEffect(Unit) {
        authViewModel.navigation.collect { destination ->
            when (destination) {
                Destination.Main -> {
                    navController.navigate(Main)
                }

                Destination.GettingStart -> {
                    navController.navigate(GettingStart)
                }
                else -> {}
            }
        }
    }
    val darkTheme = isSystemInDarkTheme()

    val logoRes = if (darkTheme) {
        R.drawable.fin_slice_logo_black_bg
    } else {
        R.drawable.fin_slice_logo_white_bg
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = logoRes),
                contentDescription = "FinSlice Logo",
                modifier = Modifier.size(100.dp),
                contentScale = ContentScale.Fit,
            )

            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    "Fin", style = MaterialTheme.typography.headlineLarge.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    "Slice", style = MaterialTheme.typography.headlineLarge.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}




