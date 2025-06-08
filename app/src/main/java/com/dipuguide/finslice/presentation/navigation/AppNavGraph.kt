package com.dipuguide.finslice.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dipuguide.finslice.presentation.screens.auth.AuthViewModel
import com.dipuguide.finslice.presentation.screens.auth.ForgetPasswordScreen
import com.dipuguide.finslice.presentation.screens.auth.GettingStartScreen
import com.dipuguide.finslice.presentation.screens.auth.SignInScreen
import com.dipuguide.finslice.presentation.screens.auth.SignUpScreen
import com.dipuguide.finslice.presentation.screens.auth.SplashScreen
import com.dipuguide.finslice.presentation.screens.history.TransactionHistoryViewModel
import com.dipuguide.finslice.presentation.screens.main.MainScreen
import com.dipuguide.finslice.presentation.screens.main.transaction.AddTransactionScreen
import com.dipuguide.finslice.presentation.screens.main.transaction.ExpenseTransactionViewModel
import com.dipuguide.finslice.presentation.screens.main.transaction.IncomeTransactionViewModel
import com.dipuguide.finslice.presentation.screens.onBoard.OnBoardingScreen
import com.dipuguide.finslice.presentation.screens.onBoard.OnBoardingViewModel

@Composable
fun AppNavGraph() {

    //Root NavController
    val rootNavController = rememberNavController()

    //Auth Viewmodel
    val authViewModel = hiltViewModel<AuthViewModel>()

    val incomeViewModel = hiltViewModel<IncomeTransactionViewModel>()
    val expenseViewModel = hiltViewModel<ExpenseTransactionViewModel>()
    val historyViewModel = hiltViewModel<TransactionHistoryViewModel>()
    val onBoardingViewModel = hiltViewModel<OnBoardingViewModel>()

    val scope = rememberCoroutineScope()

    NavHost(
        navController = rootNavController,
        startDestination = OnBoard
    ) {

        composable<Splash> {
            SplashScreen(
                authViewModel = authViewModel,
                navController = rootNavController
            )
        }

        composable<GettingStart> {
            GettingStartScreen(
                navController = rootNavController
            )
        }
        composable<OnBoard> {
            OnBoardingScreen(
                viewModel =onBoardingViewModel,
                navController = rootNavController
            )
        }
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
        //MainScreen
        composable<Main> {
            MainScreen(
                rootNavController = rootNavController,
            )
        }

        composable<AddTransaction> {
            AddTransactionScreen(
                incomeViewModel = incomeViewModel,
                expenseViewModel = expenseViewModel,
                navController = rootNavController
            )
        }
    }

}