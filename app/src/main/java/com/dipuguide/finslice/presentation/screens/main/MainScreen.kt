package com.dipuguide.finslice.presentation.screens.main

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
import com.dipuguide.finslice.presentation.screens.main.category.CategoriesScreen
import com.dipuguide.finslice.presentation.screens.main.category.CategoryViewModel
import com.dipuguide.finslice.presentation.screens.main.history.TransactionHistoryScreen
import com.dipuguide.finslice.presentation.screens.main.history.TransactionHistoryViewModel
import com.dipuguide.finslice.presentation.screens.main.home.HomeScreen
import com.dipuguide.finslice.presentation.screens.main.home.HomeViewModel
import com.dipuguide.finslice.presentation.screens.main.report.ReportScreen
import com.dipuguide.finslice.presentation.screens.main.report.ReportViewModel
import com.dipuguide.finslice.presentation.screens.main.setting.SettingScreen
import com.dipuguide.finslice.presentation.screens.main.setting.SettingViewModel
import com.dipuguide.finslice.presentation.screens.main.transaction.ExpenseTransactionViewModel
import com.dipuguide.finslice.presentation.screens.main.transaction.IncomeTransactionViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalStdlibApi::class)
@Composable
fun MainScreen(
    rootNavController: NavController,
) {

    val tabNavController = rememberNavController()

    val historyViewModel = hiltViewModel<TransactionHistoryViewModel>()
    val authViewModel = hiltViewModel<AuthViewModel>()
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val categoryViewModel = hiltViewModel<CategoryViewModel>()
    val settingViewModel = hiltViewModel<SettingViewModel>()
    val reportViewModel = hiltViewModel<ReportViewModel>()

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
            startDestination = Home,
        ) {

            composable<Home> {
                HomeScreen(
                    innerPadding = innerPadding,
                    homeViewModel = homeViewModel,
                    authViewModel = authViewModel,
                    onOverViewClick = {
                        tabNavController.navigate(Report) {
                            popUpTo(tabNavController.graph.startDestinationId) {
                                inclusive = false
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
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
                ReportScreen(
                    innerPadding = innerPadding,
                    reportViewModel = reportViewModel
                )
            }

            composable<TransactionHistory> {
                TransactionHistoryScreen(
                    innerPadding = innerPadding,
                    historyViewModel = historyViewModel
                )
            }

            composable<Setting> {
                SettingScreen(
                    innerPadding = innerPadding,
                    settingViewModel = settingViewModel,
                    authViewModel = authViewModel,
                    homeViewModel = homeViewModel,
                    navController = rootNavController
                )
            }
        }
    }
}