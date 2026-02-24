package torilab.assessment.notes.ui.screen.home

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import torilab.assessment.notes.domain.model.Note
import torilab.assessment.notes.domain.usecase.DeleteMultipleNotesUseCase
import torilab.assessment.notes.domain.usecase.DeleteNoteUseCase
import torilab.assessment.notes.domain.usecase.GetAllNotesUseCase
import torilab.assessment.notes.domain.viewstate.IViewEvent
import torilab.assessment.notes.domain.viewstate.IViewState
import torilab.assessment.notes.ui.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val deleteMultipleNotesUseCase: DeleteMultipleNotesUseCase
) : BaseViewModel<HomeState, HomeEvent>() {

    val notesFlow: Flow<PagingData<Note>> =
        getAllNotesUseCase().cachedIn(viewModelScope)

    override fun createInitialState() = HomeState()

    override fun onTriggerEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.NoteClicked -> {
                if (currentState.isSelectionMode) {
                    toggleNoteSelection(event.noteId)
                } else {
                    setEvent(HomeEvent.NavigateToEdit(event.noteId))
                }
            }
            is HomeEvent.DeleteNote -> deleteNote(event.noteId)
            is HomeEvent.NoteLongPressed -> enterSelectionMode(event.noteId)
            is HomeEvent.ToggleNoteSelection -> toggleNoteSelection(event.noteId)
            is HomeEvent.SelectAll -> selectAll(event.allNoteIds)
            is HomeEvent.DeselectAll -> setState { copy(selectedNoteIds = emptySet()) }
            is HomeEvent.ExitSelectionMode -> setState { copy(isSelectionMode = false, selectedNoteIds = emptySet()) }
            is HomeEvent.DeleteSelectedNotes -> deleteSelectedNotes()
            else -> Unit
        }
    }

    private fun enterSelectionMode(noteId: Long) {
        setState { copy(isSelectionMode = true, selectedNoteIds = setOf(noteId)) }
    }

    private fun toggleNoteSelection(noteId: Long) {
        val updated = currentState.selectedNoteIds.toMutableSet()
        if (updated.contains(noteId)) updated.remove(noteId) else updated.add(noteId)
        if (updated.isEmpty()) {
            setState { copy(isSelectionMode = false, selectedNoteIds = emptySet()) }
        } else {
            setState { copy(selectedNoteIds = updated) }
        }
    }

    private fun selectAll(allNoteIds: List<Long>) {
        setState { copy(selectedNoteIds = allNoteIds.toSet()) }
    }

    private fun deleteNote(noteId: Long) {
        viewModelScope.launch {
            call(deleteNoteUseCase(noteId)) {
                setEvent(HomeEvent.NoteDeleted)
            }
        }
    }

    private fun deleteSelectedNotes() {
        val ids = currentState.selectedNoteIds.toList()
        if (ids.isEmpty()) return
        viewModelScope.launch {
            call(deleteMultipleNotesUseCase(ids)) {
                setState { copy(isSelectionMode = false, selectedNoteIds = emptySet()) }
                setEvent(HomeEvent.NotesDeleted(ids.size))
            }
        }
    }
}

data class HomeState(
    val isSelectionMode: Boolean = false,
    val selectedNoteIds: Set<Long> = emptySet()
) : IViewState

sealed interface HomeEvent : IViewEvent {
    data class NoteClicked(val noteId: Long) : HomeEvent
    data class DeleteNote(val noteId: Long) : HomeEvent
    data class NavigateToEdit(val noteId: Long) : HomeEvent
    data object NoteDeleted : HomeEvent

    data class NoteLongPressed(val noteId: Long) : HomeEvent
    data class ToggleNoteSelection(val noteId: Long) : HomeEvent
    data class SelectAll(val allNoteIds: List<Long>) : HomeEvent
    data object DeselectAll : HomeEvent
    data object ExitSelectionMode : HomeEvent
    data object DeleteSelectedNotes : HomeEvent
    data class NotesDeleted(val count: Int) : HomeEvent
}
