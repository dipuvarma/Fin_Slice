package com.dipuguide.finslice.presentation.common.component.bottom

import com.dipuguide.finslice.R
import com.dipuguide.finslice.presentation.navigation.Categories
import com.dipuguide.finslice.presentation.navigation.Home
import com.dipuguide.finslice.presentation.navigation.Report
import com.dipuguide.finslice.presentation.navigation.Setting
import com.dipuguide.finslice.presentation.navigation.TransactionHistory

val bottomNavItemList = listOf(
    BottomNavItem(
        title = "Home",
        selectedIcon = R.drawable.home_filled,
        unSelectedIcon = R.drawable.home_outline,
        route = Home
    ),
    BottomNavItem(
        title = "Category",
        selectedIcon = R.drawable.category_filled,
        unSelectedIcon = R.drawable.category_outline,
        route = Categories
    ),
    BottomNavItem(
        title = "Report",
        selectedIcon = R.drawable.report_filled,
        unSelectedIcon = R.drawable.report_outline,
        route = Report
    ),
    BottomNavItem(
        title = "History",
        selectedIcon = R.drawable.dollar_transaction_filled,
        unSelectedIcon = R.drawable.dollar_transaction_icon,
        route = TransactionHistory
    ),
    BottomNavItem(
        title = "Settings",
        selectedIcon = R.drawable.settings_filled,
        unSelectedIcon = R.drawable.settings_outline,
        route = Setting
    )
)