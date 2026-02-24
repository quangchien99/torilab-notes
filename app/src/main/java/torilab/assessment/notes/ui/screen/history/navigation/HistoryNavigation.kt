package torilab.assessment.notes.ui.screen.history.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import torilab.assessment.notes.common.Route
import torilab.assessment.notes.ui.screen.history.HistoryScreen

@Serializable
data object History : Route()

fun NavController.navigateToHistory(
    navOptions: NavOptions? = null
) {
    this.navigate(History, navOptions)
}

fun NavGraphBuilder.historyScreen() {
    composable<History> {
        HistoryScreen()
    }
}
