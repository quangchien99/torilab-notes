package torilab.assessment.notes.ui.screen.addeditnote.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import torilab.assessment.notes.common.Route
import torilab.assessment.notes.ui.screen.addeditnote.AddEditNoteScreen

@Serializable
data object AddEditNote : Route()

fun NavController.navigateToAddEditNote(
    navOptions: NavOptions? = null
) {
    this.navigate(AddEditNote, navOptions)
}

fun NavGraphBuilder.addEditNoteScreen() {
    composable<AddEditNote> {
        AddEditNoteScreen()
    }
}
