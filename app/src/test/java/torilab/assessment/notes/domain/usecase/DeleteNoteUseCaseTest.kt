package torilab.assessment.notes.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import torilab.assessment.notes.domain.repository.NoteRepository

class DeleteNoteUseCaseTest {

    private lateinit var noteRepository: NoteRepository
    private lateinit var deleteNoteUseCase: DeleteNoteUseCase

    @Before
    fun setUp() {
        noteRepository = mockk()
        deleteNoteUseCase = DeleteNoteUseCase(noteRepository)
    }

    @Test
    fun `invoke emits Unit after successful deletion`() = runTest {
        coEvery { noteRepository.deleteNoteById(1L) } just runs

        val result = deleteNoteUseCase(1L).first()

        assertEquals(Unit, result)
    }

    @Test
    fun `invoke delegates to repository deleteNoteById`() = runTest {
        val noteId = 99L
        coEvery { noteRepository.deleteNoteById(noteId) } just runs

        deleteNoteUseCase(noteId).first()

        coVerify(exactly = 1) { noteRepository.deleteNoteById(noteId) }
    }
}
