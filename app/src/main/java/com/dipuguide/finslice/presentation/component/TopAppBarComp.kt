package com.dipuguide.finslice.presentation.component

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarComp(
    title: String,
    navigationIcon: ImageVector? = null,
    actionIcon: ImageVector? = null,
    onClickNavigationIcon: (() -> Unit)? = null,
    onClickActionIcon: (() -> Unit)? = null,
) {
    val gradient = listOf(
        MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.onBackground
    )
        TopAppBar(
            windowInsets = WindowInsets(0),
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        brush = Brush.linearGradient(gradient)
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            navigationIcon = {
                if (navigationIcon != null && onClickNavigationIcon != null) {
                    IconButton(onClick = onClickNavigationIcon) {
                        Icon(
                            imageVector = navigationIcon,
                            contentDescription = "Navigation icon"
                        )
                    }
                }
            },
            actions = {
                if (actionIcon != null && onClickActionIcon != null) {
                    IconButton(onClick = onClickActionIcon) {
                        Icon(
                            imageVector = actionIcon,
                            contentDescription = "Action icon"
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                actionIconContentColor = MaterialTheme.colorScheme.onBackground,
                titleContentColor = MaterialTheme.colorScheme.onBackground,
            )
        )
    }

