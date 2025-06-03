package com.dipuguide.finslice.presentation.screens.auth

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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dipuguide.finslice.presentation.component.FormLabel
import com.dipuguide.finslice.presentation.navigation.ForgetPassword
import com.dipuguide.finslice.presentation.navigation.Home
import com.dipuguide.finslice.presentation.navigation.SignIn
import com.dipuguide.finslice.presentation.navigation.SignUp
import com.dipuguide.finslice.utils.Destination

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
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                    viewModel.resetForm()
                }

                is AuthUiEvent.Error -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                else -> Unit
            }
        }
    }

    // Handle Navigation Events
    LaunchedEffect(Unit) {
        viewModel.navigation.collect { destination ->
            when (destination) {
                Destination.Home -> {
                    navController.navigate(Home)
                }
                Destination.SignIn -> {
                    navController.navigate(SignIn)
                }
                else -> {}
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
                text = "Login account",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
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
            Spacer(modifier = Modifier.height(32.dp))

            // Email
            FormLabel("Your email")
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
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

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
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
                enabled = !(userDetail.email.isEmpty() && userDetail.password.isEmpty())
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
                        Text("SignIn")
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

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
                        text = "Forget password",
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .clickable {
                                navController.navigate(ForgetPassword)
                            },
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    )

                }
            }
            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = {
                    navController.navigate(SignUp)
                    viewModel.resetForm()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = .2f),
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("SignUp")
            }
        }
    }
}