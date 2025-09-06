package com.dipuguide.finslice.presentation.screens.main.setting

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dipuguide.finslice.R
import com.dipuguide.finslice.presentation.common.component.AlertDialogBox
import com.dipuguide.finslice.presentation.common.component.SettingCardComp
import com.dipuguide.finslice.presentation.common.component.SettingProfileComp
import com.dipuguide.finslice.presentation.common.component.SettingSwitchCard
import com.dipuguide.finslice.presentation.common.component.TopAppBarComp
import com.dipuguide.finslice.presentation.common.state.UiState
import com.dipuguide.finslice.presentation.navigation.GettingStartRoute
import com.dipuguide.finslice.presentation.navigation.MainRoute

@Composable
fun SettingScreen(
    innerPadding: PaddingValues,
    settingViewModel: SettingViewModel,
    navController: NavController,
) {

    val uiState by settingViewModel.uiState.collectAsState()
    val isDynamicMode by settingViewModel.isDynamicModeEnabled.collectAsState()
    val isDarkMode by settingViewModel.isDarkModeEnabled.collectAsState()
    val settingUiState by settingViewModel.settingUiState.collectAsState()
    val userDetail by settingViewModel.userDetail.collectAsState()
    val context = LocalContext.current


    LaunchedEffect(uiState) {
        when (uiState) {
            is UiState.Success -> {
                val successMessage = (uiState as UiState.Success).message
                Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show()
            }

            is UiState.Error -> {
                val errorMessage = (uiState as UiState.Error).error
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }

            else -> Unit
        }
    }

    LaunchedEffect(Unit) {
        settingViewModel.navigation.collect { destination ->
            when (destination) {
                SettingNavigation.RATE -> TODO()
                SettingNavigation.PRIVACY -> TODO()
                SettingNavigation.FEEDBACK -> TODO()
                SettingNavigation.SIGN_OUT -> {
                    navController.navigate(GettingStartRoute) {
                        navController.navigate(MainRoute) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            launchSingleTop = true
                            restoreState = false
                        }
                    }
                    settingViewModel.resetUiState()
                }

                SettingNavigation.DELETE_ACCOUNT -> {
                    navController.navigate(GettingStartRoute) {
                        navController.navigate(MainRoute) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            launchSingleTop = true
                            restoreState = false
                        }
                    }
                    settingViewModel.resetUiState()
                }
            }
        }
    }

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
            name = userDetail.name,
            email = userDetail.email,
            image = userDetail.photoUri
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
                    checked = isDarkMode,
                    onCheckedChange = { isChecked ->
                        settingViewModel.onEvent(SettingEvent.DarkModeChange(isChecked))
                    }
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = .2f))
                SettingSwitchCard(
                    iconFilled = R.drawable.dynamic_color_fill,
                    iconOutline = R.drawable.dynamic_color_outline,
                    title = "Dynamic Color",
                    checked = isDynamicMode,
                    onCheckedChange = { isChecked ->
                        settingViewModel.onEvent(SettingEvent.DynamicColorChange(isChecked))
                    }
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
                    onClick = {
                        Toast.makeText(context, "App Is Updated", Toast.LENGTH_SHORT).show()
                    }
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = .2f))

                SettingCardComp(
                    icon = R.drawable.feedback_icon,
                    title = "Rate",
                    onClick = {
                        /*TODO*/
                    }
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = .2f))

                SettingCardComp(
                    icon = R.drawable.privacy_icon,
                    title = "Privacy",
                    onClick = {
                        /*TODO*/
                    }
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = .2f))

                var userGuide by remember { mutableStateOf(false) }

                SettingCardComp(
                    icon = R.drawable.guide_user_icon,
                    title = "User Guide",
                    onClick = {
                        userGuide = true
                    }
                )
                if (userGuide) {
                    AlertDialogBox(
                        onDismissRequest = {
                            userGuide = false
                        },
                        onConfirmation = {
                            userGuide = false
                        },
                        dialogTitle = "User Guide",
                        dialogText = "We keep your last 3 years of expense, including this year. Older records are automatically removed.",
                        icon = R.drawable.guide_user_icon
                    )
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = .2f))

                var isFeedback by remember { mutableStateOf(false) }

                SettingCardComp(
                    icon = R.drawable.email_icon,
                    title = "Help & Feedback",
                    onClick = {
                        isFeedback = true
                    }
                )
                if (isFeedback) {
                    AlertDialogBox(
                        onDismissRequest = {
                            isFeedback = false
                        },
                        onConfirmation = {
                            isFeedback = false
                        },
                        dialogTitle = "Help & Feedback",
                        dialogText = "For feature requests, or feedback, please email us below. We're here to help!",
                        icon = R.drawable.email_icon,
                        email = "dipuguide@gmail.com"
                    )
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = .2f))

                var signOut by remember { mutableStateOf(false) }

                SettingCardComp(
                    icon = R.drawable.sign_out_icon,
                    title = "Sign Out",
                    onClick = {
                        signOut = true
                    },
                    textColor = MaterialTheme.colorScheme.error,
                    iconColor = MaterialTheme.colorScheme.error
                )
                if (signOut) {
                    AlertDialogBox(
                        onDismissRequest = {
                            signOut = false
                        },
                        onConfirmation = {
                            settingViewModel.onEvent(SettingEvent.SignOutClick)
                            signOut = false
                        },
                        dialogTitle = "Sign Out?",
                        dialogText = "You can safely sign out. Your data is securely saved in the cloud, ensuring it's available whenever you sign in.",
                        email = userDetail.email,
                        icon = R.drawable.sign_out_icon,
                        confirmButtonColor = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError,
                        )
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        var isDeleteAccount by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.error,
                    shape = MaterialTheme.shapes.small,
                )
                .clickable {
                    isDeleteAccount = true
                },
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .wrapContentWidth()
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
        if (isDeleteAccount) {
            AlertDialogBox(
                onDismissRequest = {
                    isDeleteAccount = false
                },
                onConfirmation = {
                    settingViewModel.onEvent(SettingEvent.DeleteAccountClick)
                    isDeleteAccount = false
                },
                dialogTitle = "Deleted Account?",
                dialogText = "Your account and data will be permanently deleted. You can create a new account anytime with the same email.",
                email = userDetail.email,
                icon = R.drawable.delete_icon,
                confirmButtonColor = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                )
            )
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



