package com.dipuguide.finslice.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.ImagePainter
import com.dipuguide.finslice.R
import com.dipuguide.finslice.ui.theme.FinSliceTheme


@Preview(showSystemUi = true)
@Composable
private fun Test() {
    FinSliceTheme {
        CustomTopAppBar(
            title = "Dipu",
            image = R.drawable.image_1
        )
    }
}

@Composable
fun CustomTopAppBar(
    title: String,
    image: Any? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
) {
    val darkTheme = isSystemInDarkTheme()

    val logoRes = if (darkTheme) {
        R.drawable.fin_slice_logo_black_bg
    } else {
        R.drawable.fin_slice_logo_white_bg
    }
    val gradient = listOf(
        MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.onBackground
    )
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            // Navigation Icon
            if (image != null) {
                AsyncImage(
                    model = image,
                    contentDescription = "",
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.Transparent)
                        .clip(CircleShape),
                    contentScale = ContentScale.Fit
                )
            } else {
                AsyncImage(
                    model = logoRes,
                    contentDescription = "",
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.Transparent)
                        .clip(CircleShape),
                    contentScale = ContentScale.Fit
                )
            }

            // Title
            Text(
                text = " Hello, ${title}!",
                style = MaterialTheme.typography.titleLarge.copy(
                    brush = Brush.linearGradient(
                        gradient
                    )
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp)
            )
            // Actions
            if (actions != null) {
                Row(
                    modifier = Modifier.wrapContentWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                    content = actions
                )
            }
        }
    }
}
