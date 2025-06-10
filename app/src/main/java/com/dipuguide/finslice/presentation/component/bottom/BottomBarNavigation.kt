package com.dipuguide.finslice.presentation.component.bottom

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun BottomBarNavigation(
    navController: NavController,
    items: List<BottomNavItem>,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = modifier.height(72.dp),
        containerColor = MaterialTheme.colorScheme.background
    ) {
        var selectedItem by remember { mutableIntStateOf(0) }

        items.forEachIndexed { index, item ->
            val isSelected = selectedItem == index

            NavigationBarItem(
                label = { Text(item.title) },
                selected = isSelected,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onBackground, // Improved contrast
                    selectedTextColor = MaterialTheme.colorScheme.onBackground, // Matches selected icon
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.background
                ),
                onClick = {
                    selectedItem = index
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = false
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = if (isSelected) painterResource(id = item.selectedIcon) else painterResource(id = item.unSelectedIcon),
                        contentDescription = item.title,
                        modifier = Modifier.size(20.dp)
                    )
                },
            )
        }
    }
}