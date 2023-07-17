package com.yuriikonovalov.helia.designsystem.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yuriikonovalov.helia.R
import com.yuriikonovalov.helia.designsystem.theme.HeliaTheme
import com.yuriikonovalov.helia.designsystem.theme.LoginSocialButtonRippleTheme

private object LoginSocialButtonDefaults {
    val iconSize = 24.dp

    @Composable
    fun textColor() =
        if (HeliaTheme.theme.isDark) HeliaTheme.colors.white else HeliaTheme.colors.greyscale900

    @Composable
    fun containerColor() =
        if (HeliaTheme.theme.isDark) HeliaTheme.colors.dark2 else HeliaTheme.colors.white

    @Composable
    fun border() = BorderStroke(
        width = 1.dp,
        color = if (HeliaTheme.theme.isDark) HeliaTheme.colors.dark3 else HeliaTheme.colors.greyscale200
    )
}

@Composable
private fun LoginSocialButton(
    icon: @Composable () -> Unit,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = ButtonDefaults.outlinedButtonColors(
        containerColor = LoginSocialButtonDefaults.containerColor(),
    )

    CompositionLocalProvider(LocalRippleTheme provides LoginSocialButtonRippleTheme) {
        OutlinedButton(
            modifier = modifier,
            onClick = onClick,
            colors = colors,
            shape = HeliaTheme.shapes.medium,
            border = LoginSocialButtonDefaults.border(),
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 18.dp)
        ) {
            icon()
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                style = HeliaTheme.typography.bodyLargeSemiBold,
                color = LoginSocialButtonDefaults.textColor()
            )
        }
    }
}

@Composable
private fun LoginSocialIconButton(
    @DrawableRes iconResId: Int,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = IconButtonDefaults.outlinedIconButtonColors(
        containerColor = LoginSocialButtonDefaults.containerColor(),
    )

    CompositionLocalProvider(LocalRippleTheme provides LoginSocialButtonRippleTheme) {
        OutlinedIconButton(
            modifier = modifier.size(width = 86.dp, height = 60.dp),
            onClick = onClick,
            colors = colors,
            shape = HeliaTheme.shapes.medium,
            border = LoginSocialButtonDefaults.border(),
        ) {
            Icon(
                modifier = Modifier.size(LoginSocialButtonDefaults.iconSize),
                painter = painterResource(id = iconResId),
                contentDescription = contentDescription,
                tint = Color.Unspecified
            )
        }
    }
}

@Composable
fun LoginGoogleIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LoginSocialIconButton(
        modifier = modifier,
        iconResId = R.drawable.ic_google,
        contentDescription = stringResource(R.string.cd_google_icon),
        onClick = onClick
    )
}


@Composable
fun LoginGoogleButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LoginSocialButton(
        modifier = modifier,
        icon = {
            Icon(
                modifier = Modifier.sizeIn(LoginSocialButtonDefaults.iconSize),
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = stringResource(R.string.cd_google_icon),
                tint = Color.Unspecified
            )
        },
        text = stringResource(R.string.login_google_button_text),
        onClick = onClick
    )
}

