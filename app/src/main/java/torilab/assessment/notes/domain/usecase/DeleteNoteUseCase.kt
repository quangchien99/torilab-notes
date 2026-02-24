package torilab.assessment.notes.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import torilab.assessment.notes.domain.base.BaseUseCase
import torilab.assessment.notes.domain.repository.NoteRepository
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) : BaseUseCase<Long, Unit> {

    override suspend fun invoke(param: Long): Flow<Unit> = flow {
        noteRepository.deleteNoteById(param)
        emit(Unit)
    }
}
