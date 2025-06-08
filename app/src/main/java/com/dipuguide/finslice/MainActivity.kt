package com.dipuguide.finslice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.dipuguide.finslice.presentation.navigation.AppNavGraph
import com.dipuguide.finslice.presentation.screens.auth.GettingStartScreen
import com.dipuguide.finslice.presentation.screens.onBoard.SplashScreen
import com.dipuguide.finslice.ui.theme.FinSliceTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinSliceTheme(
                dynamicColor = false
            ) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GettingStartScreen()
                }
            }
        }
    }
}

