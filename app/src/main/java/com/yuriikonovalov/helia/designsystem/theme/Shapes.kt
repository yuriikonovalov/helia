package com.yuriikonovalov.helia.designsystem.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Immutable
data class HeliaShapes(
    val extraSmall: Shape,
    val small: Shape,
    val medium: Shape,
    val large: Shape,
    val extraLarge: Shape,
    val circular: Shape
)

val heliaShapes = HeliaShapes(
    extraSmall = RoundedCornerShape(10.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(36.dp),
    circular = CircleShape
)

val LocalHeliaShapes = staticCompositionLocalOf { heliaShapes }