package com.dipuguide.finslice.presentation.screens.auth

import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dipuguide.finslice.data.repo.DataStoreRepository
import com.dipuguide.finslice.presentation.component.FormLabel
import com.dipuguide.finslice.presentation.navigation.ForgetPassword
import com.dipuguide.finslice.presentation.navigation.GettingStart
import com.dipuguide.finslice.presentation.navigation.Main
import com.dipuguide.finslice.presentation.navigation.OnBoard
import com.dipuguide.finslice.presentation.navigation.SignUp
import com.dipuguide.finslice.presentation.screens.main.home.HomeViewModel
import com.dipuguide.finslice.utils.Destination
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(
    viewModel: AuthViewModel,
    navController: NavController,
) {
    val state by viewModel.uiState.collectAsState()
    val event by viewModel.uiEvent.collectAsState(AuthUiEvent.Idle)
    val userDetail = state.user
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    // Handle UI Events
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is AuthUiEvent.Success -> {
                    Toast.makeText(context, event.message.ifBlank { "Sign-in successful 🎉" }, Toast.LENGTH_SHORT).show()
                    viewModel.resetForm()
                }

                is AuthUiEvent.Error -> {
                    Toast.makeText(context, event.message.ifBlank { "Sign-in failed ❌" }, Toast.LENGTH_SHORT).show()
                }

                else -> Unit
            }
        }
    }

    // Handle Navigation Events
    LaunchedEffect(true) {
        viewModel.navigation.collect { destination ->
            when (destination) {
                Destination.Main -> {
                    navController.navigate(Main) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
                        restoreState = false
                    }
                }

                Destination.GettingStart -> {
                    navController.navigate(GettingStart) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
                        restoreState = false
                    }
                }

                else -> Unit
            }
        }
    }

    Box(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {
            // Header
            Text(
                text = "Sign In account",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Please enter your email or password",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = .6f),
                )
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Email
            FormLabel("Email")
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = userDetail.email,
                onValueChange = viewModel::updateEmail,
                modifier = Modifier
                    .fillMaxWidth(),
                placeholder = { Text("Enter Your Email") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email icon"
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                shape = MaterialTheme.shapes.small
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Password
            FormLabel("Password")
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = userDetail.password,
                onValueChange = viewModel::updatePassword,
                modifier = Modifier
                    .fillMaxWidth(),
                placeholder = { Text("Enter Your Password") },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = "Password icon")
                },
                trailingIcon = {
                    if (userDetail.password.isNotEmpty()) {
                        IconButton(onClick = viewModel::togglePasswordVisibility) {
                            Icon(
                                imageVector = if (state.isPasswordVisible)
                                    Icons.Default.VisibilityOff
                                else
                                    Icons.Default.RemoveRedEye,
                                contentDescription = "Toggle password visibility"
                            )
                        }
                    }
                },
                visualTransformation = if (state.isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() },
                ),
                shape = MaterialTheme.shapes.small
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = "Forget password",
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clickable {
                            navController.navigate(ForgetPassword)
                        },
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Login Button
            Button(
                onClick = {
                    viewModel.signIn(
                        email = userDetail.email,
                        password = userDetail.password
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onBackground,
                    contentColor = MaterialTheme.colorScheme.background
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
                enabled = userDetail.email.isNotBlank() && userDetail.password.isNotBlank()
            ) {
                AnimatedContent(
                    targetState = event is AuthUiEvent.Loading
                ) { loading ->
                    if (loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Sign In")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Divider(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                )

                Text(
                    text = "  Or Sign In with  ",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    textAlign = TextAlign.Center
                )

                Divider(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                )
            }

            SignInWithGoogleButton(
                onSuccess = {
                    viewModel.onLoggedIn()
                    navController.navigate(Main) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                    viewModel.saveUserDetails(
                        name = it.displayName,
                        email = it.email,
                        photo = it.photoUrl,
                        phoneNumber = it.phoneNumber
                    )
                    Toast.makeText(context, "Sign-in Success", Toast.LENGTH_SHORT).show()
                },
                onError = {
                    Toast.makeText(context, "Sign-in Failed", Toast.LENGTH_SHORT).show()
                },
                title = "Sign In with Google"
            )
            Spacer(modifier = Modifier.height(24.dp))
            // Register
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Don't have account?",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Text(
                        text = "Sign Up",
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .clickable {
                                navController.navigate(SignUp)
                                viewModel.resetForm()
                            },
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        ),
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}