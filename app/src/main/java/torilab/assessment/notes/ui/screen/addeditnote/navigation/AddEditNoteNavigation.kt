package torilab.assessment.notes.ui.screen.addeditnote.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import torilab.assessment.notes.common.Route
import torilab.assessment.notes.ui.screen.addeditnote.AddEditNoteScreen

@Serializable
data class AddEditNote(val noteId: Long? = null) : Route()

fun NavController.navigateToAddEditNote(
    noteId: Long? = null,
    navOptions: NavOptions? = null
) {
    this.navigate(AddEditNote(noteId), navOptions)
}

fun NavGraphBuilder.addEditNoteScreen(
    onNavigateBack: () -> Unit
) {
    composable<AddEditNote> {
        AddEditNoteScreen(onNavigateBack = onNavigateBack)
    }
}
