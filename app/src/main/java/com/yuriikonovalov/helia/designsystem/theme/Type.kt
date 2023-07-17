package com.yuriikonovalov.helia.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.yuriikonovalov.helia.R

private val UrbanistFontFamily = FontFamily(
    Font(R.font.urbanist_bold, weight = FontWeight.Bold),
    Font(R.font.urbanist_semibold, weight = FontWeight.SemiBold),
    Font(R.font.urbanist_medium, weight = FontWeight.Medium),
    Font(R.font.urbanist_regular, weight = FontWeight.Normal)
)

object FontSize {
    val XLarge = 18.sp
    val Large = 16.sp
    val Medium = 14.sp
    val Small = 12.sp
    val XSmall = 10.sp
}

@Immutable
data class HeliaTypography(
    val heading1: TextStyle,
    val heading2: TextStyle,
    val heading3: TextStyle,
    val heading4: TextStyle,
    val heading5: TextStyle,
    val heading6: TextStyle,

    val bodyXLargeBold: TextStyle,
    val bodyXLargeSemiBold: TextStyle,
    val bodyXLargeMedium: TextStyle,
    val bodyXLargeRegular: TextStyle,

    val bodyLargeBold: TextStyle,
    val bodyLargeSemiBold: TextStyle,
    val bodyLargeMedium: TextStyle,
    val bodyLargeRegular: TextStyle,

    val bodyMediumBold: TextStyle,
    val bodyMediumSemiBold: TextStyle,
    val bodyMediumMedium: TextStyle,
    val bodyMediumRegular: TextStyle,

    val bodySmallBold: TextStyle,
    val bodySmallSemiBold: TextStyle,
    val bodySmallMedium: TextStyle,
    val bodySmallRegular: TextStyle,

    val bodyXSmallBold: TextStyle,
    val bodyXSmallSemiBold: TextStyle,
    val bodyXSmallMedium: TextStyle,
    val bodyXSmallRegular: TextStyle
)

val heliaTypography = HeliaTypography(
    heading1 = TextStyle(
        fontFamily = UrbanistFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp
    ),
    heading2 = TextStyle(
        fontFamily = UrbanistFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp
    ),
    heading3 = TextStyle(
        fontFamily = UrbanistFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp
    ),

    heading4 = TextStyle(
        fontFamily = UrbanistFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
    heading5 = TextStyle(
        fontFamily = UrbanistFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    heading6 = TextStyle(
        fontFamily = UrbanistFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),

    bodyXLargeBold = TextStyle(
        fontFamily = UrbanistFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = FontSize.XLarge
    ),
    bodyXLargeSemiBold = TextStyle(
        fontFamily = UrbanistFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = FontSize.XLarge
    ),
    bodyXLargeMedium = TextStyle(
        fontFamily = UrbanistFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = FontSize.XLarge
    ),
    bodyXLargeRegular = TextStyle(
        fontFamily = UrbanistFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = FontSize.XLarge
    ),

    bodyLargeBold = TextStyle(
        fontFamily = UrbanistFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = FontSize.Large
    ),
    bodyLargeSemiBold = TextStyle(
        fontFamily = UrbanistFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = FontSize.Large
    ),
    bodyLargeMedium = TextStyle(
        fontFamily = UrbanistFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = FontSize.Large
    ),
    bodyLargeRegular = TextStyle(
        fontFamily = UrbanistFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = FontSize.Large
    ),

    bodyMediumBold = TextStyle(
        fontFamily = UrbanistFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = FontSize.Medium
    ),
    bodyMediumSemiBold = TextStyle(
        fontFamily = UrbanistFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = FontSize.Medium
    ),
    bodyMediumMedium = TextStyle(
        fontFamily = UrbanistFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = FontSize.Medium
    ),
    bodyMediumRegular = TextStyle(
        fontFamily = UrbanistFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = FontSize.Medium
    ),

    bodySmallBold = TextStyle(
        fontFamily = UrbanistFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = FontSize.Small
    ),
    bodySmallSemiBold = TextStyle(
        fontFamily = UrbanistFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = FontSize.Small
    ),
    bodySmallMedium = TextStyle(
        fontFamily = UrbanistFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = FontSize.Small
    ),
    bodySmallRegular = TextStyle(
        fontFamily = UrbanistFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = FontSize.Small
    ),

    bodyXSmallBold = TextStyle(
        fontFamily = UrbanistFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = FontSize.XSmall
    ),
    bodyXSmallSemiBold = TextStyle(
        fontFamily = UrbanistFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = FontSize.XSmall
    ),
    bodyXSmallMedium = TextStyle(
        fontFamily = UrbanistFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = FontSize.XSmall
    ),
    bodyXSmallRegular = TextStyle(
        fontFamily = UrbanistFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = FontSize.XSmall
    )
)

val LocalHeliaTypography = staticCompositionLocalOf { heliaTypography }