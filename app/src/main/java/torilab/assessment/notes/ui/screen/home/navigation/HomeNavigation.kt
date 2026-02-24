package torilab.assessment.notes.ui.screen.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import torilab.assessment.notes.common.Route
import torilab.assessment.notes.ui.screen.home.HomeScreen

@Serializable
data object Home : Route()

fun NavController.navigateToHome(
    navOptions: NavOptions? = null
) {
    this.navigate(Home, navOptions)
}

fun NavGraphBuilder.homeScreen(
    onNoteClick: (Long) -> Unit
) {
    composable<Home> {
        HomeScreen(onNoteClick = onNoteClick)
    }
}
