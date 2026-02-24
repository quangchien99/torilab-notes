package torilab.assessment.notes.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import torilab.assessment.notes.domain.model.Note
import torilab.assessment.notes.domain.repository.NoteRepository

class GetNoteByIdUseCaseTest {

    private lateinit var noteRepository: NoteRepository
    private lateinit var getNoteByIdUseCase: GetNoteByIdUseCase

    @Before
    fun setUp() {
        noteRepository = mockk()
        getNoteByIdUseCase = GetNoteByIdUseCase(noteRepository)
    }

    @Test
    fun `invoke emits note when found`() = runTest {
        val note = Note(id = 1L, title = "Found", content = "Content", createdAt = 100L, updatedAt = 200L)
        coEvery { noteRepository.getNoteById(1L) } returns note

        val result = getNoteByIdUseCase(1L).first()

        assertEquals("Found", result.title)
        assertEquals("Content", result.content)
    }

    @Test
    fun `invoke completes without emission when note not found`() = runTest {
        coEvery { noteRepository.getNoteById(999L) } returns null

        val results = getNoteByIdUseCase(999L).toList()

        assertTrue(results.isEmpty())
    }

    @Test
    fun `invoke delegates to repository getNoteById`() = runTest {
        val note = Note(id = 5L, title = "T", content = "C", createdAt = 0L, updatedAt = 0L)
        coEvery { noteRepository.getNoteById(5L) } returns note

        getNoteByIdUseCase(5L).first()

        coVerify(exactly = 1) { noteRepository.getNoteById(5L) }
    }
}
