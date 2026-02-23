package torilab.assessment.notes.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorPalette = darkColorScheme(
    primary = NeutralWhite,
    onPrimary = Charcoal900,
    primaryContainer = Charcoal800,
    onPrimaryContainer = NeutralWhite,

    secondary = Charcoal500,
    onSecondary = NeutralWhite,
    secondaryContainer = Charcoal700,
    onSecondaryContainer = LightGray200,

    tertiary = SoftGray,
    onTertiary = NeutralWhite,
    tertiaryContainer = Charcoal700,
    onTertiaryContainer = LightGray300,

    error = SoftRed,
    onError = DarkRed,
    errorContainer = DarkRed,
    onErrorContainer = SoftRed,

    background = Charcoal900,
    onBackground = LightGray100,

    surface = Charcoal850,
    onSurface = LightGray100,
    surfaceVariant = Charcoal800,
    onSurfaceVariant = LightGray300,

    outline = Charcoal600,
    outlineVariant = Charcoal700,

    inverseSurface = LightGray100,
    inverseOnSurface = Charcoal900,
    inversePrimary = NeutralBlack,

    scrim = Charcoal900,
)

private val LightColorPalette = lightColorScheme(
    primary = NeutralBlack,
    onPrimary = OffWhite,
    primaryContainer = LightGray200,
    onPrimaryContainer = NeutralBlack,

    secondary = LightGray400,
    onSecondary = NeutralBlack,
    secondaryContainer = LightGray100,
    onSecondaryContainer = Charcoal800,

    tertiary = SoftGray,
    onTertiary = OffWhite,
    tertiaryContainer = LightGray200,
    onTertiaryContainer = Charcoal700,

    error = MutedRed,
    onError = OffWhite,
    errorContainer = LightGray200,
    onErrorContainer = MutedRed,

    background = OffWhite,
    onBackground = NeutralBlack,

    surface = LightGray50,
    onSurface = NeutralBlack,
    surfaceVariant = LightGray100,
    onSurfaceVariant = Charcoal700,

    outline = LightGray300,
    outlineVariant = LightGray200,

    inverseSurface = Charcoal800,
    inverseOnSurface = LightGray100,
    inversePrimary = NeutralWhite,

    scrim = NeutralBlack,
)

@Composable
fun ToriNotesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorPalette
        else -> LightColorPalette
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val currentWindow = (view.context as? Activity)?.window
                ?: throw Exception("Not in an activity - unable to get Window reference")

            currentWindow.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(currentWindow, view).isAppearanceLightStatusBars =
                !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = Shapes,
        content = content
    )
}