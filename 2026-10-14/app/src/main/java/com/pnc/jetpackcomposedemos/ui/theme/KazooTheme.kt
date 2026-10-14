package com.pnc.jetpackcomposedemos.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val KazooLightColors =
    lightColorScheme(
        primary = Color(0xFF5B3F8C),
        onPrimary = Color.White,
        primaryContainer = Color(0xFFEADDFF),
        onPrimaryContainer = Color(0xFF21005D),

        secondary = Color(0xFF625B71),
        onSecondary = Color.White,
        secondaryContainer = Color(0xFFE8DEF8),
        onSecondaryContainer = Color(0xFF1D192B),

        tertiary = Color(0xFF7D5260),
        onTertiary = Color.White,

        error = Color(0xFFBA1A1A),
        onError = Color.White,

        background = Color(0xFFFFFBFE),
        onBackground = Color(0xFF1C1B1F),

        surface = Color(0xFFFFFBFE),
        onSurface = Color(0xFF1C1B1F),
    )

private val KazooDarkColors =
    darkColorScheme(
        primary = Color(0xFFD0BCFF),
        onPrimary = Color(0xFF381E72),
        primaryContainer = Color(0xFF4F378B),
        onPrimaryContainer = Color(0xFFEADDFF),

        secondary = Color(0xFFCCC2DC),
        onSecondary = Color(0xFF332D41),
        secondaryContainer = Color(0xFF4A4458),
        onSecondaryContainer = Color(0xFFE8DEF8),

        tertiary = Color(0xFFEFB8C8),
        onTertiary = Color(0xFF492532),

        error = Color(0xFFFFB4AB),
        onError = Color(0xFF690005),

        background = Color(0xFF1C1B1F),
        onBackground = Color(0xFFE6E1E5),

        surface = Color(0xFF1C1B1F),
        onSurface = Color(0xFFE6E1E5),
    )

val KazooShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(6.dp),
    medium = RoundedCornerShape(2.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(28.dp)
)


val KazooTypography = Typography(
    headlineMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 34.sp,
    ),

    titleLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 26.sp,
    ),

    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),

    labelLarge = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
    )
)


@Composable
fun KazooTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) KazooDarkColors else KazooLightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = KazooTypography,
        shapes = KazooShapes,
        content = content
    )
}












