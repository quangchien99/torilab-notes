package torilab.assessment.notes.ui.screen.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import torilab.assessment.notes.common.Route
import torilab.assessment.notes.ui.screen.settings.SettingsScreen

@Serializable
data object Settings : Route()

fun NavController.navigateToSettings(
    navOptions: NavOptions? = null
) {
    this.navigate(Settings, navOptions)
}

fun NavGraphBuilder.settingsScreen() {
    composable<Settings> {
        SettingsScreen()
    }
}
