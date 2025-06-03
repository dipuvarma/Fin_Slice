package com.dipuguide.finslice.presentation.screens.main

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.traceEventEnd
import androidx.navigation.NavController
import com.dipuguide.finslice.presentation.navigation.SignIn
import com.dipuguide.finslice.presentation.screens.auth.AuthViewModel

@Composable
fun HomeScreen(
    viewModel: AuthViewModel,
    navController: NavController
) {

    Button(onClick = {
        viewModel.signOut()
        navController.navigate(SignIn)
    }) {
        Text(text = "SignOut")
    }
}