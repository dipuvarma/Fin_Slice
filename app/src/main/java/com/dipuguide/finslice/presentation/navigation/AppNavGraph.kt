package com.dipuguide.finslice.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dipuguide.finslice.presentation.screens.addTransaction.AddTransactionScreen
import com.dipuguide.finslice.presentation.screens.addTransaction.expense.AddExpenseViewModel
import com.dipuguide.finslice.presentation.screens.addTransaction.income.AddIncomeViewModel
import com.dipuguide.finslice.presentation.screens.main.MainScreen
import com.dipuguide.finslice.presentation.screens.main.home.HomeViewModel
import com.dipuguide.finslice.presentation.screens.main.report.ReportScreen
import com.dipuguide.finslice.presentation.screens.main.report.ReportViewModel
import com.dipuguide.finslice.presentation.screens.starter.forgetPassword.ForgetPasswordScreen
import com.dipuguide.finslice.presentation.screens.starter.forgetPassword.ForgetViewModel
import com.dipuguide.finslice.presentation.screens.starter.onBoard.GettingStartScreen
import com.dipuguide.finslice.presentation.screens.starter.onBoard.OnBoardingScreen
import com.dipuguide.finslice.presentation.screens.starter.onBoard.OnBoardingViewModel
import com.dipuguide.finslice.presentation.screens.starter.signIn.SignInScreen
import com.dipuguide.finslice.presentation.screens.starter.signIn.SignInViewModel
import com.dipuguide.finslice.presentation.screens.starter.signUp.SignUpScreen
import com.dipuguide.finslice.presentation.screens.starter.signUp.SignUpViewModel
import com.dipuguide.finslice.presentation.screens.starter.splash.SplashScreen
import com.dipuguide.finslice.presentation.screens.starter.splash.SplashViewModel


@Composable
fun AppNavGraph() {

    //Root NavController
    val rootNavController = rememberNavController()

    //Viewmodel
    val signUpViewModel = hiltViewModel<SignUpViewModel>()
    val signInViewModel = hiltViewModel<SignInViewModel>()
    val forgetViewModel = hiltViewModel<ForgetViewModel>()
    val splashViewModel = hiltViewModel<SplashViewModel>()

    val onBoardingViewModel = hiltViewModel<OnBoardingViewModel>()
    val addExpenseViewModel = hiltViewModel<AddExpenseViewModel>()
    val addIncomeViewModel = hiltViewModel<AddIncomeViewModel>()
    val homeViewModel = hiltViewModel<HomeViewModel>()


    NavHost(
        navController = rootNavController,
        startDestination = SplashRoute
    ) {

        composable<SplashRoute> {
            SplashScreen(
                viewModel = splashViewModel,
                navController = rootNavController
            )
        }

        composable<GettingStartRoute> {
            GettingStartScreen(
                navController = rootNavController
            )
        }
        composable<OnBoardRoute> {
            OnBoardingScreen(
                viewModel = onBoardingViewModel,
                navController = rootNavController
            )
        }
        composable<SignInRoute> {
            SignInScreen(
                viewModel = signInViewModel,
                navController = rootNavController
            )
        }
        composable<SignUpRoute> {
            SignUpScreen(
                viewModel = signUpViewModel,
                navController = rootNavController
            )
        }
        composable<ForgetPasswordRoute> {
            ForgetPasswordScreen(
                viewModel = forgetViewModel,
                navController = rootNavController
            )
        }
        //MainScreen
        composable<MainRoute> {
            MainScreen(
                rootNavController = rootNavController,
            )
        }

        composable<AddTransactionRoute> {
            AddTransactionScreen(
                addIncomeViewModel = addIncomeViewModel,
                addExpenseViewModel = addExpenseViewModel,
                homeViewModel = homeViewModel,
                navController = rootNavController
            )
        }
    }

}