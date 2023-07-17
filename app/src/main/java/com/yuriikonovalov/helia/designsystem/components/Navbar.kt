package com.yuriikonovalov.helia.designsystem.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.yuriikonovalov.helia.R
import com.yuriikonovalov.helia.designsystem.theme.HeliaTheme

@Composable
fun TopDestinationNavbar(
    title: String,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {}
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LogoIcon()
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = HeliaTheme.typography.heading4,
            color = if (HeliaTheme.theme.isDark) HeliaTheme.colors.white else HeliaTheme.colors.greyscale900,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.width(12.dp))
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.End
        ) {
            actions()
        }
    }
}

@Composable
fun Navbar(
    title: String,
    onNavigateClick: () -> Unit,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onNavigateClick) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_left),
                contentDescription = stringResource(R.string.cd_navigate_up),
                tint = if (HeliaTheme.theme.isDark) HeliaTheme.colors.white else HeliaTheme.colors.greyscale900
            )
        }
        Text(
            text = title,
            style = HeliaTheme.typography.heading4,
            color = if (HeliaTheme.theme.isDark) HeliaTheme.colors.white else HeliaTheme.colors.greyscale900,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.width(12.dp))
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.End
        ) {
            actions()
        }
    }
}

@Composable
fun NavbarAction(
    @DrawableRes iconResId: Int,
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier,
    tint: Color = if (HeliaTheme.theme.isDark) HeliaTheme.colors.white else HeliaTheme.colors.greyscale900
) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(iconResId),
            contentDescription = contentDescription,
            tint = tint
        )
    }
}