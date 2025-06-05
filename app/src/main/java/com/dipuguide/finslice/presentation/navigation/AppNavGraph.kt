package com.dipuguide.finslice.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dipuguide.finslice.presentation.screens.auth.AuthViewModel
import com.dipuguide.finslice.presentation.screens.auth.ForgetPasswordScreen
import com.dipuguide.finslice.presentation.screens.auth.SignInScreen
import com.dipuguide.finslice.presentation.screens.auth.SignUpScreen
import com.dipuguide.finslice.presentation.screens.history.TransactionHistoryScreen
import com.dipuguide.finslice.presentation.screens.history.TransactionHistoryViewModel
import com.dipuguide.finslice.presentation.screens.main.HomeScreen
import com.dipuguide.finslice.presentation.screens.main.transaction.IncomeTransactionViewModel
import com.dipuguide.finslice.presentation.screens.main.transaction.TransactionScreen

@Composable
fun AppNavGraph() {

    //Root NavController
    val rootNavController = rememberNavController()

    //Auth Viewmodel
    val authViewModel = hiltViewModel<AuthViewModel>()

    val incomeViewModel = hiltViewModel<IncomeTransactionViewModel>()

    val historyViewModel = hiltViewModel<TransactionHistoryViewModel>()

    val scope = rememberCoroutineScope()

    NavHost(
        navController = rootNavController,
        startDestination = TransactionHistory
    ) {

        composable<SignIn> {
            SignInScreen(
                viewModel = authViewModel,
                navController = rootNavController
            )
        }
        composable<SignUp> {
            SignUpScreen(
                viewModel = authViewModel,
                navController = rootNavController
            )
        }
        composable<ForgetPassword> {
            ForgetPasswordScreen(
                viewModel = authViewModel,
                navController = rootNavController
            )
        }

        composable<Home> {
            HomeScreen(
                viewModel = authViewModel,
                navController = rootNavController,
                incomeViewModel = incomeViewModel
            )
        }
        composable<AddTransaction> {
           TransactionScreen(
               viewModel = incomeViewModel,
               navController = rootNavController
           )
        }

        composable<TransactionHistory> {
            TransactionHistoryScreen(
                historyViewModel = historyViewModel,
                incomeViewModel = incomeViewModel
            )
        }
    }

}