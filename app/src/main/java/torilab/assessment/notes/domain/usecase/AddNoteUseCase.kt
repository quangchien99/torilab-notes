package torilab.assessment.notes.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import torilab.assessment.notes.domain.base.BaseUseCase
import torilab.assessment.notes.domain.model.Note
import torilab.assessment.notes.domain.repository.NoteRepository
import javax.inject.Inject

class AddNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) : BaseUseCase<Note, Long> {

    override suspend fun invoke(param: Note): Flow<Long> = flow {
        emit(noteRepository.addNote(param))
    }
}
