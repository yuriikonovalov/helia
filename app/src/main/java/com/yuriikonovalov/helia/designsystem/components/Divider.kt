package com.yuriikonovalov.helia.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yuriikonovalov.helia.designsystem.theme.HeliaTheme

@Composable
fun Divider(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(1.dp)
            .fillMaxWidth()
            .background(if (HeliaTheme.theme.isDark) HeliaTheme.colors.dark3 else HeliaTheme.colors.greyscale200)
    )
}

@Composable
fun Divider(
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(if (HeliaTheme.theme.isDark) HeliaTheme.colors.dark3 else HeliaTheme.colors.greyscale200)
        )
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = text,
            color = if (HeliaTheme.theme.isDark) HeliaTheme.colors.greyscale100 else HeliaTheme.colors.greyscale700,
            style = HeliaTheme.typography.bodyXLargeSemiBold
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(if (HeliaTheme.theme.isDark) HeliaTheme.colors.dark3 else HeliaTheme.colors.greyscale200)
        )
    }
}