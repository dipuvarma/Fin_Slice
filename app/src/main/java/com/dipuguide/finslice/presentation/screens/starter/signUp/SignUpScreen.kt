package com.dipuguide.finslice.presentation.screens.starter.signUp

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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dipuguide.finslice.domain.model.User
import com.dipuguide.finslice.presentation.common.component.FormLabel
import com.dipuguide.finslice.presentation.common.component.PasswordStrengthMeter
import com.dipuguide.finslice.presentation.common.component.SignInWithGoogleButton
import com.dipuguide.finslice.presentation.common.state.UiState
import com.dipuguide.finslice.presentation.navigation.OnBoardRoute
import com.dipuguide.finslice.presentation.navigation.SignInRoute

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel,
    navController: NavController,
) {

    val uiState by viewModel.uiState.collectAsState()
    val signUpUiState by viewModel.signUpUiState.collectAsState()
    val isFormValid by viewModel.isFormValid.collectAsState()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current


    LaunchedEffect(uiState) {

        when (uiState) {
            is UiState.Success -> {
                val successMessage = (uiState as UiState.Success).message
                Toast.makeText(
                    context,
                    successMessage.ifBlank { "Sign-up successful 🎉" },
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.resetPasswordStrengthMessage()
                viewModel.resetUiEvent()
            }

            is UiState.Error -> {
                val errorMessage = (uiState as UiState.Error).error
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                viewModel.resetPasswordStrengthMessage()
                viewModel.resetUiEvent()
            }

            else -> Unit

        }
    }

    LaunchedEffect(Unit) {
        viewModel.navigation.collect { destination ->
            when (destination) {
                SignUpNavigation.OnBoard -> {
                    navController.navigate(OnBoardRoute) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
                        restoreState = false
                    }
                }

                SignUpNavigation.SignIn -> {
                    navController.navigate(SignInRoute) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
                        restoreState = false
                    }
                }
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
                text = "Create account",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = MaterialTheme.colorScheme.onBackground,
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
            Spacer(modifier = Modifier.height(24.dp))

            //Name
            FormLabel("Name")
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = signUpUiState.name,
                onValueChange = { name ->
                    viewModel.onEvent(SignUpEvent.NameChange(name))
                },
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
                shape = MaterialTheme.shapes.small,
                isError = signUpUiState.name.isNotBlank() && signUpUiState.nameError != null,
                singleLine = true,
            )
            if (signUpUiState.name.isNotBlank()) {
                signUpUiState.nameError?.let {
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
            FormLabel("Email")
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = signUpUiState.email,
                onValueChange = { email ->
                    viewModel.onEvent(SignUpEvent.EmailChange(email))
                },
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
                shape = MaterialTheme.shapes.small,
                isError = signUpUiState.email.isNotBlank() && signUpUiState.emailError != null,
                singleLine = true
            )
            if (signUpUiState.email.isNotBlank()) {
                signUpUiState.emailError?.let {
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
                value = signUpUiState.password,
                onValueChange = { password ->
                    viewModel.onEvent(SignUpEvent.PasswordChange(password))
                },
                modifier = Modifier
                    .fillMaxWidth(),
                placeholder = { Text("Enter Your Password") },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = "Password icon")
                },
                trailingIcon = {
                    if (signUpUiState.password.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                viewModel.onEvent(SignUpEvent.PasswordVisibility)
                            }
                        ) {
                            Icon(
                                imageVector = if (signUpUiState.isPasswordVisible)
                                    Icons.Default.VisibilityOff
                                else
                                    Icons.Default.RemoveRedEye,
                                contentDescription = "Toggle password visibility"
                            )
                        }
                    }
                },
                visualTransformation = if (signUpUiState.isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                shape = MaterialTheme.shapes.small,
                isError = signUpUiState.password.isNotBlank() && signUpUiState.showPasswordErrors && signUpUiState.passwordErrors.isNotEmpty(),
                singleLine = true,
            )
            if (signUpUiState.password.isNotBlank() && signUpUiState.showPasswordErrors && signUpUiState.passwordErrors.isNotEmpty()) {
                signUpUiState.passwordErrors.forEach {
                    Text(
                        text = "• $it",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                    )
                }
            }
            if (signUpUiState.password.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                PasswordStrengthMeter(signUpUiState.passwordStrength)
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Register Button
            Button(
                onClick = {
                    viewModel.onEvent(SignUpEvent.SignUpClick)
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
                enabled = isFormValid
            ) {
                AnimatedContent(
                    targetState = (uiState is UiState.Loading)
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
                    text = "  Or Sign Up with  ",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    textAlign = TextAlign.Center
                )

                HorizontalDivider(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp),
                    thickness = DividerDefaults.Thickness,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                )
            }

            SignInWithGoogleButton(
                onSuccess = {
                    /*TODO: Offline User Details Save*/
                    val user = User(
                        name = it.displayName ?: "Unknown",
                        email = it.email ?: "N/A",
                        photoUri = it.photoUrl.toString()
                    )
                    viewModel.saveUserDetail(user)
                    viewModel.onEvent(SignUpEvent.SignUpWithGoogle)
                    Toast.makeText(context, "Sign-Up Success", Toast.LENGTH_SHORT).show()
                },
                onError = {
                    Toast.makeText(context, "Sign-Up Failed", Toast.LENGTH_SHORT).show()
                },
                title = "Sign Up with Google"
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Login Prompt
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
                        text = "Already have an account?",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Text(
                        text = "Sign In",
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .clickable {
                                viewModel.onEvent(SignUpEvent.SignInClick)
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