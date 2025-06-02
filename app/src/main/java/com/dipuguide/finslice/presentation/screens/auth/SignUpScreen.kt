package com.dipuguide.finslice.presentation.screens.auth

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dipuguide.finslice.presentation.component.FormLabel
import com.dipuguide.finslice.presentation.component.PasswordStrengthMeter
import com.dipuguide.finslice.presentation.navigation.Home
import com.dipuguide.finslice.presentation.navigation.SignIn
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    viewModel: AuthViewModel,
    navController: NavController,
) {

    val context = LocalContext.current
    val state by viewModel.authUiState.collectAsState()
    val event by viewModel.authUiEvent.collectAsState()
    val userDetail = state.user
    val focusManager = LocalFocusManager.current

    LaunchedEffect(event) {
        when (event) {
            is AuthUiEvent.Success -> {
                Toast.makeText(context, (event as AuthUiEvent.Success).message, Toast.LENGTH_SHORT)
                    .show()
                viewModel.resetPasswordStrengthMessage()
                // navigate
                navController.navigate(Home)
                //reset here
                viewModel.clearEvents()
                //reset Inputs
                viewModel.resetNameEmailOrPass()
            }

            is AuthUiEvent.Error -> {
                Toast.makeText(context, (event as AuthUiEvent.Error).message, Toast.LENGTH_SHORT)
                    .show()
                viewModel.clearEvents()
            }

            else -> {}
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
                text = "Create account",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Please enter your details",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = .6f),
                )
            )
            Spacer(modifier = Modifier.height(32.dp))

            //Name
            FormLabel("Your Name")
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = userDetail.name,
                onValueChange = { viewModel.updateName(it) },
                modifier = Modifier
                    .fillMaxWidth(),
                placeholder = { Text("Enter Your Name") },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = "Person icon")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.Words
                ),
                isError = state.user.name.isNotBlank() && state.nameError != null,
                singleLine = true,
            )
            if (state.user.name.isNotBlank()) {
                state.nameError?.let {
                    Text(
                        text = "• $it",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Email
            FormLabel("Your email")
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = userDetail.email,
                onValueChange = { viewModel.updateEmail(it) },
                modifier = Modifier
                    .fillMaxWidth(),
                placeholder = { Text("Enter Your Email") },
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = "Email icon")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                isError = state.user.email.isNotBlank() && state.emailError != null,
                singleLine = true
            )
            if (state.user.email.isNotBlank()) {
                state.emailError?.let {
                    Text(
                        text = "• $it",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Password
            FormLabel("Password")
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = userDetail.password,
                onValueChange = { viewModel.updatePassword(it) },
                modifier = Modifier
                    .fillMaxWidth(),
                placeholder = { Text("Enter Your Password") },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = "Password icon")
                },
                trailingIcon = {
                    if (userDetail.password.isNotEmpty()) {
                        IconButton(
                            onClick = viewModel::togglePasswordVisibility
                        ) {
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
                    onDone = { focusManager.clearFocus() }
                ),
                isError = state.user.password.isNotBlank() && state.showPasswordErrors && state.passwordErrors.isNotEmpty(),
                singleLine = true,
            )
            if (state.user.password.isNotBlank() && state.showPasswordErrors && state.passwordErrors.isNotEmpty()) {
                state.passwordErrors.forEach {
                    Text(
                        text = "• $it",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                    )
                }
            }
            if (state.user.password.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                PasswordStrengthMeter(state.passwordStrength)
            }
            Spacer(modifier = Modifier.height(32.dp))

            // Register Button
            Button(
                onClick = {
                    viewModel.signUp(
                        name = userDetail.name,
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
                enabled = viewModel.isFormValid()
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
                        Text(text = "SignUp")
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))

            // Login Prompt
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Already have an account?",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    navController.navigate(SignIn)
                    viewModel.resetNameEmailOrPass()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = .2f),
                    contentColor = MaterialTheme.colorScheme.primary
                ),
            ) {
                Text("SignIn")
            }
        }
    }
}