package com.yuriikonovalov.helia.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.yuriikonovalov.helia.designsystem.theme.HeliaTheme


private object ToggleDefaults {
    @Composable
    fun thumbColor() = HeliaTheme.colors.white

    @Composable
    fun checkedTrackColor() = HeliaTheme.colors.primary500

    @Composable
    fun uncheckedTrackColor() =
        if (HeliaTheme.theme.isDark) HeliaTheme.colors.dark2 else HeliaTheme.colors.greyscale200
}

@Composable
fun Toggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = SwitchDefaults.colors(
        checkedIconColor = Color.Transparent,
        checkedThumbColor = ToggleDefaults.thumbColor(),
        uncheckedThumbColor = ToggleDefaults.thumbColor(),
        checkedTrackColor = ToggleDefaults.checkedTrackColor(),
        uncheckedTrackColor = ToggleDefaults.uncheckedTrackColor(),
        uncheckedBorderColor = ToggleDefaults.uncheckedTrackColor()
    )
    Switch(
        modifier = modifier,
        checked = checked,
        onCheckedChange = onCheckedChange,
        colors = colors,
        thumbContent = { Box(modifier = Modifier) }
    )
}