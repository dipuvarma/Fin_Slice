package com.dipuguide.finslice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.dipuguide.finslice.presentation.navigation.AppNavGraph
import com.dipuguide.finslice.presentation.screens.main.setting.SettingViewModel
import com.dipuguide.finslice.presentation.ui.theme.FinSliceTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val settingViewModel: SettingViewModel = hiltViewModel()
            val isDarkMode by settingViewModel.isDarkModeEnabled.collectAsState()
            val isDynamicMode by settingViewModel.isDynamicModeEnabled.collectAsState()

            FinSliceTheme(darkTheme = isDarkMode, dynamicColor = isDynamicMode) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavGraph()
                }
            }
        }
    }
}

