package com.yuriikonovalov.helia.designsystem.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yuriikonovalov.helia.designsystem.theme.HeliaTheme

@Immutable
data class ChipValues(
    val horizontalPadding: Dp,
    val verticalPadding: Dp,
    val itemSpacer: Dp,
    val iconSize: Dp
)

private object ChipColorDefaults {
    @Composable
    fun containerColor(toggled: Boolean) =
        if (toggled) HeliaTheme.colors.primary500 else Color.Transparent

    @Composable
    fun contentColor(toggled: Boolean) =
        if (toggled) HeliaTheme.colors.white else HeliaTheme.colors.primary500

    @Composable
    fun borderColor() = HeliaTheme.colors.primary500
}

sealed interface ChipSizeValues {
    val values: ChipValues

    @Composable
    fun textStyle(): TextStyle

    object Small : ChipSizeValues {
        override val values = ChipValues(16.dp, 6.dp, 8.dp, 12.dp)

        @Composable
        override fun textStyle() = HeliaTheme.typography.bodyMediumSemiBold
    }

    object Medium : ChipSizeValues {
        override val values = ChipValues(20.dp, 8.dp, 8.dp, 16.dp)

        @Composable
        override fun textStyle() = HeliaTheme.typography.bodyLargeSemiBold
    }

    object Large : ChipSizeValues {
        override val values = ChipValues(24.dp, 10.dp, 8.dp, 20.dp)

        @Composable
        override fun textStyle() = HeliaTheme.typography.bodyXLargeBold
    }

}

@Composable
fun Chip(
    toggled: Boolean,
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    chipSize: ChipSizeValues = ChipSizeValues.Medium,
    @DrawableRes leadingIconResId: Int? = null,
    @DrawableRes trailingIconResId: Int? = null,
) {
    Box(
        modifier = modifier
            .border(2.dp, ChipColorDefaults.borderColor(), CircleShape)
            .background(ChipColorDefaults.containerColor(toggled), CircleShape)
            .clip(CircleShape)
            .clickable(role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.padding(
                chipSize.values.horizontalPadding,
                chipSize.values.verticalPadding
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            leadingIconResId?.let { id ->
                Icon(
                    modifier = Modifier.size(chipSize.values.iconSize),
                    painter = painterResource(id),
                    contentDescription = null,
                    tint = ChipColorDefaults.contentColor(toggled = toggled)
                )
                Spacer(modifier = Modifier.width(chipSize.values.itemSpacer))
            }
            Text(
                text = text,
                style = chipSize.textStyle(),
                color = ChipColorDefaults.contentColor(toggled = toggled)
            )
            trailingIconResId?.let { id ->
                Spacer(modifier = Modifier.width(chipSize.values.itemSpacer))
                Icon(
                    modifier = Modifier.size(chipSize.values.iconSize),
                    painter = painterResource(id),
                    contentDescription = null,
                    tint = ChipColorDefaults.contentColor(toggled = toggled)
                )
            }
        }
    }
}