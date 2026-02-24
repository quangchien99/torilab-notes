package torilab.assessment.notes.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf

data class ThemeState(
    val isDarkMode: Boolean,
    val onToggleDarkMode: () -> Unit
)

val LocalThemeState = staticCompositionLocalOf<ThemeState> {
    error("No ThemeState provided")
}
