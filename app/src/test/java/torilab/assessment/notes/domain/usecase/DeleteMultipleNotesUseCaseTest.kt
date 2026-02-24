package torilab.assessment.notes.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import torilab.assessment.notes.domain.repository.NoteRepository
import org.junit.Assert.assertEquals

class DeleteMultipleNotesUseCaseTest {

    private lateinit var noteRepository: NoteRepository
    private lateinit var deleteMultipleNotesUseCase: DeleteMultipleNotesUseCase

    @Before
    fun setUp() {
        noteRepository = mockk()
        deleteMultipleNotesUseCase = DeleteMultipleNotesUseCase(noteRepository)
    }

    @Test
    fun `invoke emits Unit after deleting multiple notes`() = runTest {
        val ids = listOf(1L, 2L, 3L)
        coEvery { noteRepository.deleteNotesByIds(ids) } just runs

        val result = deleteMultipleNotesUseCase(ids).first()

        assertEquals(Unit, result)
    }

    @Test
    fun `invoke delegates to repository deleteNotesByIds`() = runTest {
        val ids = listOf(10L, 20L)
        coEvery { noteRepository.deleteNotesByIds(ids) } just runs

        deleteMultipleNotesUseCase(ids).first()

        coVerify(exactly = 1) { noteRepository.deleteNotesByIds(ids) }
    }

    @Test
    fun `invoke handles empty list`() = runTest {
        val ids = emptyList<Long>()
        coEvery { noteRepository.deleteNotesByIds(ids) } just runs

        deleteMultipleNotesUseCase(ids).first()

        coVerify(exactly = 1) { noteRepository.deleteNotesByIds(ids) }
    }
}
