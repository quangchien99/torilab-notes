package torilab.assessment.notes.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import torilab.assessment.notes.domain.model.Note
import torilab.assessment.notes.domain.repository.NoteRepository
import javax.inject.Inject

class GetAllNotesUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(): Flow<PagingData<Note>> {
        return noteRepository.getAllNotes()
    }
}
