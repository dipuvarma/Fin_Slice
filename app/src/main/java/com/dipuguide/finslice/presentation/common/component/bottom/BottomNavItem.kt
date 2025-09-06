package com.dipuguide.finslice.presentation.common.component.bottom

data class BottomNavItem(
    val title: String,
    val selectedIcon: Int,
    val unSelectedIcon: Int,
    val route: Any,
)