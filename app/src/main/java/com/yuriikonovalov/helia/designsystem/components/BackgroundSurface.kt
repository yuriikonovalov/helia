package com.yuriikonovalov.helia.designsystem.components

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.yuriikonovalov.helia.designsystem.theme.HeliaTheme

@Composable
fun BackgroundSurface(
    modifier: Modifier = Modifier,
    color: Color = HeliaTheme.backgroundColor,
    content: @Composable () -> Unit
) {
    Surface(modifier = modifier, color = color, content = content)
}