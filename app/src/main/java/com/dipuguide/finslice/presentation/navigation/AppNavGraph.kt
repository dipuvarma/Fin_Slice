package com.dipuguide.finslice.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dipuguide.finslice.presentation.screens.auth.AuthViewModel
import com.dipuguide.finslice.presentation.screens.auth.ForgetPasswordScreen
import com.dipuguide.finslice.presentation.screens.auth.SignInScreen
import com.dipuguide.finslice.presentation.screens.auth.SignUpScreen
import com.dipuguide.finslice.presentation.screens.main.HomeScreen

@Composable
fun AppNavGraph() {


    //Root NavController
    val rootNavController = rememberNavController()

    //Auth Viewmodel
    val authViewModel = hiltViewModel<AuthViewModel>()



    NavHost(
        navController = rootNavController,
        startDestination = SignIn
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
            HomeScreen()
        }
    }

}