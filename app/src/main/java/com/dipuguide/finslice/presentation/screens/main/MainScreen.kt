package com.dipuguide.finslice.presentation.screens.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dipuguide.finslice.presentation.common.component.bottom.BottomBarNavigation
import com.dipuguide.finslice.presentation.navigation.AddTransactionRoute
import com.dipuguide.finslice.presentation.navigation.Categories
import com.dipuguide.finslice.presentation.navigation.Home
import com.dipuguide.finslice.presentation.navigation.Report
import com.dipuguide.finslice.presentation.navigation.Setting
import com.dipuguide.finslice.presentation.navigation.TransactionHistory
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

@Composable
fun MainScreen(
    rootNavController: NavController,
) {

    val tabNavController = rememberNavController()
    val navBackStackEntry by tabNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val homeViewModel = hiltViewModel<HomeViewModel>()
    val categoryViewModel = hiltViewModel<CategoryViewModel>()
    val historyViewModel = hiltViewModel<TransactionHistoryViewModel>()
    val settingViewModel = hiltViewModel<SettingViewModel>()
    val reportViewModel = hiltViewModel<ReportViewModel>()

    Scaffold(
        bottomBar = {
            BottomBarNavigation(tabNavController = tabNavController)
        },
        floatingActionButton = {
            if (currentDestination?.hasRoute<Home>() == true) {
                FloatingActionButton(
                    onClick = {
                        rootNavController.navigate(AddTransactionRoute)
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
                    onOverViewClick = {
                        tabNavController.navigate(Report) {
                            popUpTo(tabNavController.graph.findStartDestination().id) {
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
                    reportViewModel = reportViewModel,
                )
            }

            composable<TransactionHistory> {
                TransactionHistoryScreen(
                    innerPadding = innerPadding,
                    historyViewModel = historyViewModel,
                )
            }

            composable<Setting> {
                SettingScreen(
                    innerPadding = innerPadding,
                    settingViewModel = settingViewModel,
                    navController = rootNavController
                )
            }
        }
    }
}