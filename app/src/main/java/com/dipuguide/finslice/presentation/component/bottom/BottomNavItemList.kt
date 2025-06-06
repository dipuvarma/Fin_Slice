package com.dipuguide.finslice.presentation.component.bottom

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Wallet
import com.dipuguide.finslice.presentation.navigation.Categories
import com.dipuguide.finslice.presentation.navigation.Home
import com.dipuguide.finslice.presentation.navigation.Setting
import com.dipuguide.finslice.presentation.navigation.TransactionHistory

val bottomNavItemList = listOf(
    BottomNavItem(
        title = "Home",
        selectedIcon = Icons.Filled.Home,
        unSelectedIcon = Icons.Outlined.Home,
        route = Home
    ),
    BottomNavItem(
        title = "Categories",
        selectedIcon = Icons.Filled.Category,
        unSelectedIcon = Icons.Outlined.Category,
        route = Categories
    ),
    BottomNavItem(
        title = "Transactions",
        selectedIcon = Icons.Filled.Wallet,
        unSelectedIcon = Icons.Outlined.Wallet,
        route = TransactionHistory
    ),
    BottomNavItem(
        title = "Setting",
        selectedIcon = Icons.Filled.Settings,
        unSelectedIcon = Icons.Outlined.Settings,
        route = Setting
    )
)