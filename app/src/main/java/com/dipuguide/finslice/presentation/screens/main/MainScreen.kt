package com.dipuguide.finslice.presentation.screens.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dipuguide.finslice.presentation.component.bottom.BottomBarNavigation
import com.dipuguide.finslice.presentation.component.bottom.bottomNavItemList
import com.dipuguide.finslice.presentation.navigation.AddTransaction
import com.dipuguide.finslice.presentation.navigation.Categories
import com.dipuguide.finslice.presentation.navigation.Home
import com.dipuguide.finslice.presentation.navigation.Report
import com.dipuguide.finslice.presentation.navigation.Setting
import com.dipuguide.finslice.presentation.navigation.TransactionHistory
import com.dipuguide.finslice.presentation.screens.auth.AuthViewModel
import com.dipuguide.finslice.presentation.screens.main.history.TransactionHistoryScreen
import com.dipuguide.finslice.presentation.screens.main.history.TransactionHistoryViewModel
import com.dipuguide.finslice.presentation.screens.main.category.CategoriesScreen
import com.dipuguide.finslice.presentation.screens.main.category.CategoryViewModel
import com.dipuguide.finslice.presentation.screens.main.home.HomeScreen
import com.dipuguide.finslice.presentation.screens.main.home.HomeViewModel
import com.dipuguide.finslice.presentation.screens.main.report.ReportScreen
import com.dipuguide.finslice.presentation.screens.main.setting.SettingScreen
import com.dipuguide.finslice.presentation.screens.main.transaction.ExpenseTransactionViewModel
import com.dipuguide.finslice.presentation.screens.main.transaction.IncomeTransactionViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalStdlibApi::class)
@Composable
fun MainScreen(
    rootNavController: NavController,
) {

    val tabNavController = rememberNavController()

    val incomeViewModel = hiltViewModel<IncomeTransactionViewModel>()
    val expenseViewModel = hiltViewModel<ExpenseTransactionViewModel>()
    val historyViewModel = hiltViewModel<TransactionHistoryViewModel>()
    val authViewModel = hiltViewModel<AuthViewModel>()
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val categoryViewModel = hiltViewModel<CategoryViewModel>()

    val currentBackStackEntry = tabNavController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry.value?.destination

    Scaffold(
        bottomBar = {
            BottomBarNavigation(
                navController = tabNavController,
                items = bottomNavItemList
            )
        },
        floatingActionButton = {
            if (currentDestination?.route?.contains("Home") == true) {
                FloatingActionButton(
                    onClick = {
                        rootNavController.navigate(AddTransaction)
                    },
                    contentColor = MaterialTheme.colorScheme.background,
                    containerColor = MaterialTheme.colorScheme.onBackground
                ) {
                    Icon(Icons.Default.Add, contentDescription = "")
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = tabNavController,
            startDestination = Setting,
        ) {

            composable<Home> {
                HomeScreen(
                    innerPadding = innerPadding,
                    homeViewModel = homeViewModel,
                    onOverViewClick = {
                        tabNavController.navigate(Report)
                    }
                )
            }

            composable<Categories> {
                CategoriesScreen(
                    innerPadding = innerPadding,
                    categoryViewModel = categoryViewModel
                )
            }

            composable<Report> {
                ReportScreen(innerPadding = innerPadding)
            }

            composable<TransactionHistory> {
                TransactionHistoryScreen(
                    innerPadding = innerPadding,
                    historyViewModel = historyViewModel
                )
            }

            composable<Setting> {
                SettingScreen(innerPadding = innerPadding)
            }
        }
    }
}