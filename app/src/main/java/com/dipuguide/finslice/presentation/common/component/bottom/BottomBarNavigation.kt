package com.dipuguide.finslice.presentation.common.component.bottom

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.dipuguide.finslice.R
import com.dipuguide.finslice.presentation.navigation.Categories
import com.dipuguide.finslice.presentation.navigation.Home
import com.dipuguide.finslice.presentation.navigation.Report
import com.dipuguide.finslice.presentation.navigation.Setting
import com.dipuguide.finslice.presentation.navigation.TransactionHistory
import kotlin.reflect.KClass

sealed class BottomBarDestination<T : Any>(
    val route: T,
    val selectedIcon: Int,
    val unselectedIcon: Int,
    val label: String,
    val contentDescription: String,
    val routeClass: KClass<T>,
) {
    data object HomeBottomBar : BottomBarDestination<Home>(
        Home,
        R.drawable.home_filled,
        R.drawable.home_outline,
        "Home",
        "Home Screen",
        Home::class
    )

    data object CategoriesBottomBar : BottomBarDestination<Categories>(
        Categories,
        R.drawable.category_filled,
        R.drawable.category_outline,
        "Categories",
        "Categories Screen",
        Categories::class
    )

    data object ReportBottomBar : BottomBarDestination<Report>(
        Report,
        R.drawable.report_filled,
        R.drawable.report_outline,
        "Report",
        "Report Screen",
        Report::class
    )

    data object HistoryBottomBar : BottomBarDestination<TransactionHistory>(
        TransactionHistory,
        R.drawable.dollar_transaction_filled,
        R.drawable.dollar_transaction_icon,
        "History",
        "History Screen",
        TransactionHistory::class
    )

    data object SettingsBottomBar : BottomBarDestination<Setting>(
        Setting,
        R.drawable.settings_filled,
        R.drawable.settings_outline,
        "Settings",
        "Settings Screen",
        Setting::class
    )
}


@Composable
fun BottomBarNavigation(
    tabNavController: NavController,
) {
    val bottomBarDestinations =
        listOf(
            BottomBarDestination.HomeBottomBar,
            BottomBarDestination.CategoriesBottomBar,
            BottomBarDestination.ReportBottomBar,
            BottomBarDestination.HistoryBottomBar,
            BottomBarDestination.SettingsBottomBar,
        )

    val navBackStackEntry by tabNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        bottomBarDestinations.forEach { destination ->
            val isSelected = currentDestination?.hasRoute(destination.routeClass) == true
            NavigationBarItem(
                selected = isSelected,
                icon = {
                    Icon(
                        painter = if (isSelected) painterResource(id = destination.selectedIcon) else painterResource(
                            id = destination.unselectedIcon
                        ),
                        contentDescription = destination.contentDescription,
                        modifier = Modifier.size(20.dp)
                    )
                },
                onClick = {
                    if (!isSelected) {
                        tabNavController.navigate(destination.route) {
                            popUpTo(tabNavController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                label = {
                    Text(
                        text = destination.label,
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onBackground, // Improved contrast
                    selectedTextColor = MaterialTheme.colorScheme.onBackground, // Matches selected icon
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.background
                ),
            )
        }
    }

}