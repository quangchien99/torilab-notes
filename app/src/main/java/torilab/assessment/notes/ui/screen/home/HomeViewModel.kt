package torilab.assessment.notes.ui.screen.home

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import torilab.assessment.notes.domain.model.Note
import torilab.assessment.notes.domain.usecase.DeleteNoteUseCase
import torilab.assessment.notes.domain.usecase.GetAllNotesUseCase
import torilab.assessment.notes.domain.viewstate.IViewEvent
import torilab.assessment.notes.domain.viewstate.IViewState
import torilab.assessment.notes.ui.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) : BaseViewModel<HomeState, HomeEvent>() {

    val notesFlow: Flow<PagingData<Note>> =
        getAllNotesUseCase().cachedIn(viewModelScope)

    override fun createInitialState() = HomeState

    override fun onTriggerEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.NoteClicked -> setEvent(HomeEvent.NavigateToEdit(event.noteId))
            is HomeEvent.DeleteNote -> deleteNote(event.noteId)
            else -> Unit
        }
    }

    private fun deleteNote(noteId: Long) {
        viewModelScope.launch {
            call(deleteNoteUseCase(noteId)) {
                setEvent(HomeEvent.NoteDeleted)
            }
        }
    }
}

data object HomeState : IViewState

sealed interface HomeEvent : IViewEvent {
    data class NoteClicked(val noteId: Long) : HomeEvent
    data class DeleteNote(val noteId: Long) : HomeEvent
    data class NavigateToEdit(val noteId: Long) : HomeEvent
    data object NoteDeleted : HomeEvent
}
