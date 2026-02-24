package torilab.assessment.notes.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import torilab.assessment.notes.domain.base.BaseUseCase
import torilab.assessment.notes.domain.model.Note
import torilab.assessment.notes.domain.repository.NoteRepository
import javax.inject.Inject

class GetNoteByIdUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) : BaseUseCase<Long, Note> {

    override suspend fun invoke(param: Long): Flow<Note> = flow {
        noteRepository.getNoteById(param)?.let { emit(it) }
    }
}
