package com.dipuguide.finslice.presentation.screens.main

import android.util.Log
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.traceEventEnd
import androidx.navigation.NavController
import com.dipuguide.finslice.data.model.IncomeTransaction
import com.dipuguide.finslice.presentation.navigation.SignIn
import com.dipuguide.finslice.presentation.screens.auth.AuthViewModel

@Composable
fun HomeScreen(
    viewModel: AuthViewModel,
    transactionViewModel: IncomeTransactionViewModel,
    navController: NavController,
) {

    val state by transactionViewModel.getIncomeUiState.collectAsState()

    LaunchedEffect(Unit) {
        Log.d("TAG", "HomeScreen: ${state.incomeTransactionList.size}")
    }

    Button(onClick = {
        viewModel.signOut()
        navController.navigate(SignIn)
    }) {
        Text(text = "SignOut")
    }
}

