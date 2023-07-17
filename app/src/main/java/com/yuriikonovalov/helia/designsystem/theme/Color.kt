package com.yuriikonovalov.helia.designsystem.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Stable
data class HeliaColorSchema(
    // Main
    val primary500: Color = Color(0xFF1AB65C),
    val primary400: Color = Color(0xFF48C57D),
    val primary300: Color = Color(0xFF76D39D),
    val primary200: Color = Color(0xFFA3E2BE),
    val primary100: Color = Color(0xFFE8F8EF),

    val secondary500: Color = Color(0xFFFFD300),
    val secondary400: Color = Color(0xFFFFDC33),
    val secondary300: Color = Color(0xFFFFE566),
    val secondary200: Color = Color(0xFFFFED99),
    val secondary100: Color = Color(0xFFFFFBE6),

    // Alert & Status
    val success: Color = Color(0xFF4ADE80),
    val info: Color = Color(0xFF246BFD),
    val warning: Color = Color(0xFFFACC15),
    val error: Color = Color(0xFFF75555),
    val disabled: Color = Color(0xFFD8D8D8),
    val buttonDisabled: Color = Color(0xFF53A777),

    // Greyscale
    val greyscale900: Color = Color(0xFF212121),
    val greyscale800: Color = Color(0xFF424242),
    val greyscale700: Color = Color(0xFF616161),
    val greyscale600: Color = Color(0xFF757575),
    val greyscale500: Color = Color(0xFF9E9E9E),
    val greyscale400: Color = Color(0xFFBDBDBD),
    val greyscale300: Color = Color(0xFFE0E0E0),
    val greyscale200: Color = Color(0xFFEEEEEE),
    val greyscale100: Color = Color(0xFFF5F5F5),
    val greyscale50: Color = Color(0xFFFAFAFA),

    // Gradients
    val gradientGreen: List<Color> = listOf(Color(0xFF39E180), Color(0xFF1AB65C)),
    val gradientBlue: List<Color> = listOf(Color(0xFF246BFD), Color(0xFF6F9EFF)),
    val gradientYellow: List<Color> = listOf(Color(0xFFFACC15), Color(0xFFFFE580)),
    val gradientRed: List<Color> = listOf(Color(0xFFFF4D67), Color(0xFFFF8A9B)),
    val gradientDark: List<Color> = listOf(Color(0x003A3A3A), Color(0xFF2C2C2C)),


    // Dark Colors
    val dark1: Color = Color(0xFF181A20),
    val dark2: Color = Color(0xFF1F222A),
    val dark3: Color = Color(0xFF35383F),

    // Others
    val white: Color = Color(0xFFFFFFFF),
    val black: Color = Color(0xFF000000),
    val red: Color = Color(0xFFF54336),
    val pink: Color = Color(0xFFEA1E61),
    val purple: Color = Color(0xFF9D28AC),
    val deepPurple: Color = Color(0xFF673AB3),
    val indigo: Color = Color(0xFF3F51B2),
    val blue: Color = Color(0xFF1A96F0),
    val lightBlue: Color = Color(0xFF00A9F1),
    val cyan: Color = Color(0xFF00BCD3),
    val teal: Color = Color(0xFF009689),
    val green: Color = Color(0xFF4AAF57),
    val lightGreen: Color = Color(0xFF8BC255),
    val lime: Color = Color(0xFFCDDC4C),
    val yellow: Color = Color(0xFFFFEB4F),
    val amber: Color = Color(0xFFFFC02D),
    val orange: Color = Color(0xFFFF981F),
    val deepOrange: Color = Color(0xFFFF5726),
    val brown: Color = Color(0xFF7A5548),
    val blueGrey: Color = Color(0xFF607D8A),

    // Background
    val backgroundGreen: Color = Color(0xFFF6FFFA),
    val backgroundBlue: Color = Color(0xFFF6FAFD),
    val backgroundPink: Color = Color(0xFFFFF5F5),
    val backgroundYellow: Color = Color(0xFFFFFEE0),
    val backgroundPurple: Color = Color(0xFFFCF4FF)

)

val heliaColorSchema = HeliaColorSchema()
val LocalHeliaColorSchema = staticCompositionLocalOf { heliaColorSchema }
