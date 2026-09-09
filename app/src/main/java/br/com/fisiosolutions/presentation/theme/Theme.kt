package br.com.fisiosolutions.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlueDark,
    onPrimary = White,
    secondary = AccentGreen,
    onSecondary = DarkBackground,
    tertiary = AccentYellow,
    onTertiary = DarkBackground,
    background = DarkBackground,
    onBackground = DarkMainText,
    surface = DarkCardSurface,
    onSurface = DarkMainText,
    surfaceVariant = DarkCardSurface,
    onSurfaceVariant = DarkSecondaryText,
    outline = DarkSecondaryText
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = White,
    secondary = AccentGreen,
    onSecondary = DarkText,
    tertiary = AccentYellow,
    onTertiary = DarkText,
    background = White,
    onBackground = DarkText,
    surface = White,
    onSurface = DarkText,
    surfaceVariant = LightGrayInput,
    onSurfaceVariant = GrayText,
    outline = InputBorder
)

@Composable
fun FisioSolutionsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color set to false by default to retain custom branding colors
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
