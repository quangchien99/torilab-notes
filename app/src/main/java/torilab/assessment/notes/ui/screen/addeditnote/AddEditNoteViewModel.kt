package torilab.assessment.notes.ui.screen.addeditnote

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import torilab.assessment.notes.domain.model.Note
import torilab.assessment.notes.domain.usecase.AddNoteUseCase
import torilab.assessment.notes.domain.usecase.GetNoteByIdUseCase
import torilab.assessment.notes.domain.usecase.UpdateNoteUseCase
import torilab.assessment.notes.domain.viewstate.IViewEvent
import torilab.assessment.notes.domain.viewstate.IViewState
import torilab.assessment.notes.ui.base.BaseViewModel
import torilab.assessment.notes.ui.screen.addeditnote.navigation.AddEditNote
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val addNoteUseCase: AddNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val getNoteByIdUseCase: GetNoteByIdUseCase
) : BaseViewModel<AddEditNoteState, AddEditNoteEvent>() {

    init {
        val route = savedStateHandle.toRoute<AddEditNote>()
        route.noteId?.let { id ->
            setState { copy(isEditMode = true, noteId = id, isLoading = true) }
            loadNote(id)
        }
    }

    override fun createInitialState() = AddEditNoteState()

    override fun onTriggerEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.TitleChanged -> setState { copy(title = event.title) }
            is AddEditNoteEvent.ContentChanged -> setState { copy(content = event.content) }
            is AddEditNoteEvent.SaveNote -> saveNote()
            else -> Unit
        }
    }

    private fun loadNote(id: Long) {
        viewModelScope.launch {
            call(getNoteByIdUseCase(id)) { note ->
                setState {
                    copy(
                        title = note.title,
                        content = note.content,
                        createdAt = note.createdAt,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun saveNote() {
        val state = currentState
        if (state.title.isBlank()) {
            setEvent(AddEditNoteEvent.ShowError("Title cannot be empty"))
            return
        }

        viewModelScope.launch {
            setState { copy(isSaving = true) }

            if (state.isEditMode && state.noteId != null) {
                val note = Note(
                    id = state.noteId,
                    title = state.title.trim(),
                    content = state.content.trim(),
                    createdAt = state.createdAt,
                    updatedAt = System.currentTimeMillis()
                )
                call(updateNoteUseCase(note)) {
                    setEvent(AddEditNoteEvent.NoteSaved)
                }
            } else {
                val note = Note(
                    title = state.title.trim(),
                    content = state.content.trim()
                )
                call(addNoteUseCase(note)) {
                    setEvent(AddEditNoteEvent.NoteSaved)
                }
            }
        }
    }
}

data class AddEditNoteState(
    val title: String = "",
    val content: String = "",
    val isEditMode: Boolean = false,
    val noteId: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false
) : IViewState

sealed interface AddEditNoteEvent : IViewEvent {
    data class TitleChanged(val title: String) : AddEditNoteEvent
    data class ContentChanged(val content: String) : AddEditNoteEvent
    data object SaveNote : AddEditNoteEvent
    data object NoteSaved : AddEditNoteEvent
    data class ShowError(val message: String) : AddEditNoteEvent
}
