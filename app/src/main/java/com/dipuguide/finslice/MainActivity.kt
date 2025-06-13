package com.dipuguide.finslice

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.dipuguide.finslice.presentation.navigation.AppNavGraph
import com.dipuguide.finslice.presentation.screens.main.setting.SettingViewModel
import com.dipuguide.finslice.ui.theme.FinSliceTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingViewModel : SettingViewModel = hiltViewModel()
            val isDarkMode = settingViewModel.isDarkModeState.collectAsState()
            val isDynamicMode = settingViewModel.isDynamicModeState.collectAsState()
            Log.d("TAG", "onCreate: $isDarkMode")
            FinSliceTheme(
                dynamicColor = isDynamicMode.value,
                darkTheme = isDarkMode.value
            ) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavGraph()
                }
            }
        }
    }
}

