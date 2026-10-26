package com.example.appescuelampaz.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = MentaDark,
    secondary = MentaMedium,
    tertiary = MentaLight
)

private val LightColorScheme = lightColorScheme(
    primary = MentaDark,
    onPrimary = Color.White,
    primaryContainer = MentaContainer,
    onPrimaryContainer = Color(0xFF143B35),
    secondary = MentaMedium,
    onSecondary = Color.White,
    background = MentaBackground,
    surface = MentaVariant,
    surfaceVariant = MentaSurface,
    onSurfaceVariant = Color(0xFF1C423C)
)

@Composable
fun AppEscuelaMPAZTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}