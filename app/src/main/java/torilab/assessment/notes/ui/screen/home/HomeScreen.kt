package torilab.assessment.notes.ui.screen.home

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import torilab.assessment.notes.R
import torilab.assessment.notes.domain.model.Note
import torilab.assessment.notes.ui.screen.component.DeleteConfirmDialog
import torilab.assessment.notes.ui.screen.home.component.HomeContent
import torilab.assessment.notes.ui.screen.home.component.NoteSearchBar
import torilab.assessment.notes.ui.screen.home.component.SelectionTopBar

@Composable
fun HomeScreen(
    onNoteClick: (Long) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val notes = viewModel.notesFlow.collectAsLazyPagingItems()
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    var noteToDelete by remember { mutableStateOf<Note?>(null) }
    var showBatchDeleteDialog by remember { mutableStateOf(false) }
    val noteDeletedMessage = stringResource(R.string.message_note_deleted)
    val notesDeletedMessage = stringResource(R.string.message_notes_deleted)

    BackHandler(enabled = state.isSelectionMode) {
        viewModel.onTriggerEvent(HomeEvent.ExitSelectionMode)
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is HomeEvent.NavigateToEdit -> onNoteClick(event.noteId)
                is HomeEvent.NoteDeleted -> snackbarHostState.showSnackbar(noteDeletedMessage)
                is HomeEvent.NotesDeleted -> snackbarHostState.showSnackbar(
                    notesDeletedMessage.format(event.count)
                )

                else -> Unit
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            AnimatedVisibility(
                visible = state.isSelectionMode,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                SelectionTopBar(
                    selectedCount = state.selectedNoteIds.size,
                    onClose = { viewModel.onTriggerEvent(HomeEvent.ExitSelectionMode) },
                    onSelectAll = {
                        viewModel.onTriggerEvent(HomeEvent.SelectAll)
                    },
                    onDelete = { showBatchDeleteDialog = true }
                )
            }

            AnimatedVisibility(visible = !state.isSelectionMode) {
                val hasNotes = notes.itemCount > 0 || state.searchQuery.isNotBlank()

                Column {
                    Text(
                        text = stringResource(R.string.screen_title_home),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(
                            start = 16.dp,
                            top = 16.dp,
                            end = 16.dp,
                            bottom = if (hasNotes) 4.dp else 8.dp
                        )
                    )

                    if (hasNotes) {
                        NoteSearchBar(
                            query = state.searchQuery,
                            onQueryChange = { viewModel.onTriggerEvent(HomeEvent.SearchQueryChanged(it)) },
                            onClear = { viewModel.onTriggerEvent(HomeEvent.ClearSearch) }
                        )
                    }
                }
            }

            HomeContent(
                notes = notes,
                isSearching = state.searchQuery.isNotBlank(),
                isSelectionMode = state.isSelectionMode,
                selectedNoteIds = state.selectedNoteIds,
                onNoteClick = { viewModel.onTriggerEvent(HomeEvent.NoteClicked(it)) },
                onNoteLongClick = { viewModel.onTriggerEvent(HomeEvent.NoteLongPressed(it)) },
                onEditClick = { viewModel.onTriggerEvent(HomeEvent.NoteClicked(it)) },
                onShareClick = { note ->
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_SUBJECT, note.title)
                        putExtra(Intent.EXTRA_TEXT, "${note.title}\n\n${note.content}")
                    }
                    context.startActivity(Intent.createChooser(shareIntent, null))
                },
                onDeleteClick = { note -> noteToDelete = note }
            )
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

    noteToDelete?.let { note ->
        DeleteConfirmDialog(
            onConfirm = {
                viewModel.onTriggerEvent(HomeEvent.DeleteNote(note.id))
                noteToDelete = null
            },
            onDismiss = { noteToDelete = null }
        )
    }

    if (showBatchDeleteDialog) {
        DeleteConfirmDialog(
            noteCount = state.selectedNoteIds.size,
            onConfirm = {
                viewModel.onTriggerEvent(HomeEvent.DeleteSelectedNotes)
                showBatchDeleteDialog = false
            },
            onDismiss = { showBatchDeleteDialog = false }
        )
    }
}
