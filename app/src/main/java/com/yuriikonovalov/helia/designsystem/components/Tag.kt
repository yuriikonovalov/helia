package com.yuriikonovalov.helia.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.yuriikonovalov.helia.designsystem.theme.HeliaTheme

enum class TagStyle {
    SOLID, OUTLINED, BORDERLESS, INVERTED
}

enum class TagState {
    DEFAULT, INFO, SUCCESS, WARNING, ERROR
}

@Composable
private fun solidTagColors(color: Color): TagColors {
    return TagColors(
        contentColor = HeliaTheme.colors.white,
        containerColor = color,
        borderColor = Color.Transparent
    )
}

@Composable
private fun outlinedTagColors(color: Color): TagColors {
    return TagColors(
        contentColor = color,
        containerColor = Color.Transparent,
        borderColor = color
    )
}

@Composable
private fun borderlessTagColors(color: Color): TagColors {
    return TagColors(
        contentColor = color,
        containerColor = Color.Transparent,
        borderColor = Color.Transparent
    )
}

@Composable
private fun invertedTagColors(color: Color, invertedColor: Color = Color(0xFF2A2B39)): TagColors {
    val containerColor = if (HeliaTheme.theme.isDark)
        invertedColor.copy(alpha = 0.24f)
    else
        color.copy(alpha = 0.12f)
    return TagColors(
        contentColor = color,
        containerColor = containerColor,
        borderColor = Color.Transparent
    )
}

private object StateColors {
    val default
        @Composable get() = HeliaTheme.colors.greyscale600
    val success
        @Composable get() = HeliaTheme.colors.success
    val info
        @Composable get() = HeliaTheme.colors.primary500
    val warning
        @Composable get() = HeliaTheme.colors.warning
    val error
        @Composable get() = HeliaTheme.colors.error
}


@Immutable
private data class TagColors(
    val containerColor: Color = Color.Transparent,
    val contentColor: Color = Color.Transparent,
    val borderColor: Color = Color.Transparent
)


private object TagDefaults {
    @Composable
    fun of(state: TagState, style: TagStyle) = when (state) {
        TagState.DEFAULT -> defaultTagColors(style)
        TagState.INFO -> infoTagColors(style)
        TagState.SUCCESS -> successTagColors(style)
        TagState.WARNING -> warningTagColors(style)
        TagState.ERROR -> errorTagColors(style)
    }

    @Composable
    private fun defaultTagColors(style: TagStyle) = when (style) {
        TagStyle.SOLID -> solidTagColors(StateColors.default)
        TagStyle.OUTLINED -> outlinedTagColors(StateColors.default)
        TagStyle.BORDERLESS -> borderlessTagColors(StateColors.default)
        TagStyle.INVERTED -> invertedTagColors(StateColors.default)
    }

    @Composable
    private fun infoTagColors(style: TagStyle) = when (style) {
        TagStyle.SOLID -> solidTagColors(StateColors.info)
        TagStyle.OUTLINED -> outlinedTagColors(StateColors.info)
        TagStyle.BORDERLESS -> borderlessTagColors(StateColors.info)
        TagStyle.INVERTED -> invertedTagColors(StateColors.info)
    }

    @Composable
    private fun successTagColors(style: TagStyle) = when (style) {
        TagStyle.SOLID -> solidTagColors(StateColors.success)
        TagStyle.OUTLINED -> outlinedTagColors(StateColors.success)
        TagStyle.BORDERLESS -> borderlessTagColors(StateColors.success)
        TagStyle.INVERTED -> invertedTagColors(StateColors.success)
    }

    @Composable
    private fun warningTagColors(style: TagStyle) = when (style) {
        TagStyle.SOLID -> solidTagColors(StateColors.warning)
        TagStyle.OUTLINED -> outlinedTagColors(StateColors.warning)
        TagStyle.BORDERLESS -> borderlessTagColors(StateColors.warning)
        TagStyle.INVERTED -> invertedTagColors(StateColors.warning)
    }

    @Composable
    private fun errorTagColors(style: TagStyle) = when (style) {
        TagStyle.SOLID -> solidTagColors(StateColors.error)
        TagStyle.OUTLINED -> outlinedTagColors(StateColors.error)
        TagStyle.BORDERLESS -> borderlessTagColors(StateColors.error)
        TagStyle.INVERTED -> invertedTagColors(StateColors.error)
    }

}


@Composable
fun Tag(
    text: String,
    style: TagStyle,
    state: TagState,
    modifier: Modifier = Modifier
) {
    val colors = TagDefaults.of(state, style)
    Box(
        modifier = modifier
            .background(colors.containerColor, RoundedCornerShape(6.dp))
            .border(1.dp, colors.borderColor, RoundedCornerShape(6.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            text = text,
            style = HeliaTheme.typography.bodyXSmallSemiBold,
            color = colors.contentColor
        )
    }
}