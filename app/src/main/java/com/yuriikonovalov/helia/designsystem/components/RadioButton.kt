package com.yuriikonovalov.helia.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.yuriikonovalov.helia.designsystem.theme.HeliaTheme


private object RadioButtonDefaults {
    val size = 24.dp
    val shape = CircleShape

    @Composable
    fun selectedColor() = HeliaTheme.colors.primary500

    @Composable
    fun unselectedColor() = HeliaTheme.colors.primary500

    @Composable
    fun textColor() =
        if (HeliaTheme.theme.isDark) HeliaTheme.colors.white else HeliaTheme.colors.greyscale900
}

@Composable
fun RadioButton(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RadioButtonDefaults.shape)
            .size(RadioButtonDefaults.size)
            .border(
                3.dp,
                RadioButtonDefaults.unselectedColor(),
                RadioButtonDefaults.shape
            )
            .padding(5.dp)
            .clip(RadioButtonDefaults.shape)
            .then(if (selected) Modifier.background(RadioButtonDefaults.selectedColor()) else Modifier)
            .clickable(
                role = Role.RadioButton,
                onClick = onClick
            )
    )
}

@Composable
fun RadioButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            style = HeliaTheme.typography.bodyMediumSemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = RadioButtonDefaults.textColor()
        )
    }
}