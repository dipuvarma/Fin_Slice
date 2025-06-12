package com.dipuguide.finslice.presentation.screens.main.setting

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dipuguide.finslice.R
import com.dipuguide.finslice.presentation.component.SettingCardComp
import com.dipuguide.finslice.presentation.component.SettingProfileComp
import com.dipuguide.finslice.presentation.component.SettingSwitchCard
import com.dipuguide.finslice.presentation.component.TopAppBarComp

@Composable
fun SettingScreen(
    innerPadding: PaddingValues,
    settingViewModel: SettingViewModel = hiltViewModel(),
) {
    val darkTheme = isSystemInDarkTheme()
    val isDarkMode = settingViewModel.isDarkModeState.collectAsState()

    Log.d("TAG", "SettingScreen: $isDarkMode")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {

        TopAppBarComp(title = "Settings")

        Spacer(modifier = Modifier.height(12.dp))

        SettingProfileComp(
            name = "Dipu Verma",
            email = "dipuverma@gmail.com"
        )
        Spacer(modifier = Modifier.height(24.dp))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 2.dp,
            contentColor = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Column {
                SettingSwitchCard(
                    iconFilled = R.drawable.dark_mode_icon,
                    iconOutline = R.drawable.light_mode_icone,
                    title = "Dark Mode",
                    checked = isDarkMode.value,
                    onCheckedChange = { isChecked ->
                        settingViewModel.toggleDarkMode(isChecked)
                    }
                )

                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = .2f))
                SettingSwitchCard(
                    iconFilled = R.drawable.dynamic_color_fill,
                    iconOutline = R.drawable.dynamic_color_outline,
                    title = "Dynamic Color",
                    checked = true,
                    onCheckedChange = { /* TODO */ }
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = .2f))
                SettingSwitchCard(
                    iconFilled = R.drawable.lock_icon,
                    iconOutline = R.drawable.unlock_icon,
                    title = "App Lock",
                    checked = false,
                    onCheckedChange = { /* TODO */ }
                )
            }

        }
        Spacer(modifier = Modifier.height(12.dp))
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 2.dp,
            contentColor = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Column {
                SettingCardComp(
                    icon = R.drawable.update_check_icon,
                    title = "Check for update",
                    onClick = {}
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = .2f))

                SettingCardComp(
                    icon = R.drawable.feedback_icon,
                    title = "Rate",
                    onClick = {}
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = .2f))

                SettingCardComp(
                    icon = R.drawable.privacy_icon,
                    title = "Privacy",
                    onClick = {}
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = .2f))

                SettingCardComp(
                    icon = R.drawable.guide_user_icon,
                    title = "User Guide",
                    onClick = {}
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = .2f))

                SettingCardComp(
                    icon = R.drawable.info_icon,
                    title = "Help & Feedback",
                    onClick = {}
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = .2f))

                SettingCardComp(
                    icon = R.drawable.sign_out_icon,
                    title = "Sign Out",
                    onClick = {

                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.error,
                    shape = MaterialTheme.shapes.small,
                )
                .clickable { },
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .wrapContentWidth() // ⬅️ Only as wide as needed
            ) {
                Text(
                    text = "Delete Account & Data",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onError
                    )
                )
                Icon(
                    painter = painterResource(R.drawable.delete_icon),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onError
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Version 1.0.0",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = .7f)
                )
            )
        }
    }
}



