package torilab.assessment.notes.ui.screen.addeditnote

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import torilab.assessment.notes.ui.screen.addeditnote.component.AddEditNoteContent

@Composable
fun AddEditNoteScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddEditNoteViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is AddEditNoteEvent.NoteSaved -> onNavigateBack()
                is AddEditNoteEvent.NoteDeleted -> onNavigateBack()
                is AddEditNoteEvent.ShowError -> snackbarHostState.showSnackbar(event.message)
                else -> Unit
            }
        }
    }

    AddEditNoteContent(
        state = state,
        snackbarHostState = snackbarHostState,
        showDeleteDialog = showDeleteDialog,
        onNavigateBack = onNavigateBack,
        onTitleChanged = { viewModel.onTriggerEvent(AddEditNoteEvent.TitleChanged(it)) },
        onContentChanged = { viewModel.onTriggerEvent(AddEditNoteEvent.ContentChanged(it)) },
        onSave = { viewModel.onTriggerEvent(AddEditNoteEvent.SaveNote) },
        onDeleteRequest = { showDeleteDialog = true },
        onDeleteConfirm = {
            viewModel.onTriggerEvent(AddEditNoteEvent.DeleteNote)
            showDeleteDialog = false
        },
        onDeleteDismiss = { showDeleteDialog = false }
    )
}
