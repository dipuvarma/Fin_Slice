package com.dipuguide.finslice.presentation.screens.starter.splash

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dipuguide.finslice.R
import com.dipuguide.finslice.presentation.navigation.GettingStartRoute
import com.dipuguide.finslice.presentation.navigation.MainRoute

@Composable
fun SplashScreen(
    viewModel: SplashViewModel,
    navController: NavController,
) {

    val context = LocalContext.current
    val navigationState by viewModel.navigation.collectAsState()

    LaunchedEffect(navigationState) {
        val destination = navigationState ?: return@LaunchedEffect

        when (destination) {
            SplashNavigation.Main -> {
                navController.navigate(MainRoute) {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    launchSingleTop = true
                    restoreState = false
                }
            }

            SplashNavigation.GettingStart -> {
                navController.navigate(GettingStartRoute) {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                    launchSingleTop = true
                    restoreState = false
                }
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