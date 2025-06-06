package com.dipuguide.finslice.presentation.screens.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dipuguide.finslice.data.repo.ExpenseTransactionRepo
import com.dipuguide.finslice.data.repo.FirebaseAuthRepository
import com.dipuguide.finslice.data.repo.IncomeTransactionRepo
import com.dipuguide.finslice.presentation.component.TopAppBarComp
import com.dipuguide.finslice.presentation.component.bottom.BottomBarNavigation
import com.dipuguide.finslice.presentation.component.bottom.bottomNavItemList
import com.dipuguide.finslice.presentation.navigation.Categories
import com.dipuguide.finslice.presentation.navigation.Home
import com.dipuguide.finslice.presentation.navigation.Setting
import com.dipuguide.finslice.presentation.navigation.TransactionHistory
import com.dipuguide.finslice.presentation.screens.auth.AuthViewModel
import com.dipuguide.finslice.presentation.screens.history.TransactionHistoryScreen
import com.dipuguide.finslice.presentation.screens.history.TransactionHistoryViewModel
import com.dipuguide.finslice.presentation.screens.main.transaction.ExpenseTransactionViewModel
import com.dipuguide.finslice.presentation.screens.main.transaction.IncomeTransactionViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    rootNavController: NavController,
) {

    val tabNavController = rememberNavController()

    val incomeViewModel = hiltViewModel<IncomeTransactionViewModel>()
    val expenseViewModel = hiltViewModel<ExpenseTransactionViewModel>()
    val historyViewModel = hiltViewModel<TransactionHistoryViewModel>()
    val authViewModel = hiltViewModel<AuthViewModel>()

    val currentBackStackEntry = tabNavController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry.value?.destination

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Fin Slice",
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
            )
        },
        bottomBar = {
            BottomBarNavigation(
                navController = tabNavController,
                items = bottomNavItemList
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = tabNavController,
            startDestination = Home,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable<Home> {
                HomeScreen(
                    viewModel = authViewModel,
                    navController = rootNavController,
                    incomeViewModel = incomeViewModel,
                    expenseViewModel = expenseViewModel
                )
            }

            composable<Categories> {
                CategoriesScreen()
            }


            composable<TransactionHistory> {
                TransactionHistoryScreen(
                    historyViewModel = historyViewModel,
                    incomeViewModel = incomeViewModel,
                    expenseViewModel = expenseViewModel
                )
            }

            composable<Setting> {
                SettingScreen()
            }
        }
    }
}