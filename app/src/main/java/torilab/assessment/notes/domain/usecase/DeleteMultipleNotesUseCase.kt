package torilab.assessment.notes.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import torilab.assessment.notes.domain.repository.NoteRepository
import javax.inject.Inject

class DeleteMultipleNotesUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(noteIds: List<Long>): Flow<Unit> = flow {
        noteRepository.deleteNotesByIds(noteIds)
        emit(Unit)
    }
}
