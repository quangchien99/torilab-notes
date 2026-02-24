package torilab.assessment.notes.ui.screen.settings

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import torilab.assessment.notes.domain.model.Note
import torilab.assessment.notes.domain.repository.NoteRepository
import torilab.assessment.notes.domain.viewstate.IViewEvent
import torilab.assessment.notes.domain.viewstate.IViewState
import torilab.assessment.notes.ui.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : BaseViewModel<SettingsState, SettingsEvent>() {

    override fun createInitialState() = SettingsState()

    override fun onTriggerEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.GenerateBulkNotes -> generateBulkNotes()
            else -> Unit
        }
    }

    private fun generateBulkNotes() {
        if (currentState.isGenerating) return

        viewModelScope.launch {
            setState { copy(isGenerating = true, generatedCount = 0) }

            withContext(Dispatchers.IO) {
                val batchSize = 100
                val total = 1000

                for (i in 0 until total step batchSize) {
                    val batch = (i until (i + batchSize).coerceAtMost(total)).map { index ->
                        val now = System.currentTimeMillis()
                        Note(
                            title = "Test Note #${index + 1}",
                            content = "This is auto-generated test note number ${index + 1}. " +
                                    "Created for testing pagination and performance.",
                            createdAt = now - (total - index) * 1000L,
                            updatedAt = now - (total - index) * 1000L
                        )
                    }
                    noteRepository.addNotes(batch)
                    setState { copy(generatedCount = i + batchSize) }
                }
            }

            setState { copy(isGenerating = false) }
            setEvent(SettingsEvent.BulkNotesGenerated)
        }
    }
}

data class SettingsState(
    val isGenerating: Boolean = false,
    val generatedCount: Int = 0
) : IViewState

sealed interface SettingsEvent : IViewEvent {
    data object GenerateBulkNotes : SettingsEvent
    data object BulkNotesGenerated : SettingsEvent
}
