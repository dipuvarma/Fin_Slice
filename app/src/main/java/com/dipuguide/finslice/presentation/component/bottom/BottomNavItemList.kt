package com.dipuguide.finslice.presentation.component.bottom

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Wallet
import com.dipuguide.finslice.presentation.navigation.Categories
import com.dipuguide.finslice.presentation.navigation.Home
import com.dipuguide.finslice.presentation.navigation.Report
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
        title = "Category",
        selectedIcon = Icons.Filled.Category,
        unSelectedIcon = Icons.Outlined.Category,
        route = Categories
    ),
    BottomNavItem(
        title = "Report",
        selectedIcon = Icons.AutoMirrored.Filled.TrendingUp,
        unSelectedIcon = Icons.AutoMirrored.Outlined.TrendingUp,
        route = Report
    ),
    BottomNavItem(
        title = "Journal",
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