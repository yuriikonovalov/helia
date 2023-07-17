package com.yuriikonovalov.helia.designsystem.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yuriikonovalov.helia.designsystem.theme.HeliaTheme

private object PrimaryButtonDefaults {
    @Composable
    fun containerColor() = HeliaTheme.colors.primary500

    @Composable
    fun contentColor() = HeliaTheme.colors.white


    @Composable
    fun disabledContainerColor() = HeliaTheme.colors.buttonDisabled
}

private object SecondaryButtonDefaults {
    val IconSize = 20.dp

    @Composable
    fun containerColor() =
        if (HeliaTheme.theme.isDark) HeliaTheme.colors.dark3 else HeliaTheme.colors.primary100

    @Composable
    fun contentColor() =
        if (HeliaTheme.theme.isDark) HeliaTheme.colors.white else HeliaTheme.colors.primary500
}


private fun Modifier.drawColoredShadow(
    color: Color = Color(0xFF1AB65C),
    alpha: Float = 0.25f,
    shadowRadius: Dp = 24.dp,
    offsetX: Dp = 4.dp,
    offsetY: Dp = 8.dp,
    borderRadius: Dp = 0.dp
) = this.drawBehind {
    val transparentColor = android.graphics.Color.toArgb(color.copy(alpha = 0.0f).value.toLong())
    val shadowColor = android.graphics.Color.toArgb(color.copy(alpha = alpha).value.toLong())
    this.drawIntoCanvas {
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.color = transparentColor
        frameworkPaint.setShadowLayer(
            shadowRadius.toPx(),
            offsetX.toPx(),
            offsetY.toPx(),
            shadowColor
        )
        it.drawRoundRect(
            0f,
            0f,
            this.size.width,
            this.size.height,
            borderRadius.toPx(),
            borderRadius.toPx(),
            paint
        )
    }
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = HeliaTheme.shapes.circular,
    enabled: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 18.dp)
) {
    val colors = ButtonDefaults.buttonColors(
        containerColor = PrimaryButtonDefaults.containerColor(),
        contentColor = PrimaryButtonDefaults.contentColor(),
        disabledContainerColor = PrimaryButtonDefaults.disabledContainerColor(),
        disabledContentColor = PrimaryButtonDefaults.contentColor(),
    )

    val elevation = ButtonDefaults.elevatedButtonElevation(
        defaultElevation = 4.dp
    )

    ElevatedButton(
        modifier = modifier
            .then(if (enabled) Modifier.drawColoredShadow() else Modifier),
        shape = shape,
        colors = colors,
        enabled = enabled,
        onClick = onClick,
        elevation = elevation,
        contentPadding = contentPadding
    ) {
        Text(
            text = text,
            style = HeliaTheme.typography.bodyLargeBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes leadingIconResId: Int? = null,
    @DrawableRes trailingIconResId: Int? = null,
    shape: Shape = HeliaTheme.shapes.circular,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 18.dp)
) {
    val colors = ButtonDefaults.buttonColors(
        containerColor = SecondaryButtonDefaults.containerColor(),
        contentColor = SecondaryButtonDefaults.contentColor()
    )

    Button(
        modifier = modifier,
        shape = shape,
        colors = colors,
        onClick = onClick,
        contentPadding = contentPadding
    ) {
        leadingIconResId?.let { id ->
            Icon(
                modifier = Modifier.size(SecondaryButtonDefaults.IconSize),
                painter = painterResource(id),
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(16.dp))
        }
        Text(
            text = text,
            style = HeliaTheme.typography.bodyLargeBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        trailingIconResId?.let { id ->
            Spacer(modifier = Modifier.width(16.dp))
            Icon(
                modifier = Modifier.size(SecondaryButtonDefaults.IconSize),
                painter = painterResource(id),
                contentDescription = null
            )
        }
    }
}

@Composable
fun TextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = HeliaTheme.colors.primary500
) {
    Text(
        modifier = modifier
            .clip(HeliaTheme.shapes.extraSmall)
            .clickable(
                onClick = onClick,
                role = Role.Button
            )
            .padding(2.dp),
        text = text,
        style = HeliaTheme.typography.bodyMediumSemiBold,
        color = color
    )
}