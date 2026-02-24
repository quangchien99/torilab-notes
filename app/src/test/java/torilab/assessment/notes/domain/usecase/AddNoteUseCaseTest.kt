package torilab.assessment.notes.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import torilab.assessment.notes.domain.model.Note
import torilab.assessment.notes.domain.repository.NoteRepository

class AddNoteUseCaseTest {

    private lateinit var noteRepository: NoteRepository
    private lateinit var addNoteUseCase: AddNoteUseCase

    @Before
    fun setUp() {
        noteRepository = mockk()
        addNoteUseCase = AddNoteUseCase(noteRepository)
    }

    @Test
    fun `invoke emits id returned by repository`() = runTest {
        val note = Note(title = "Test", content = "Content")
        coEvery { noteRepository.addNote(note) } returns 42L

        val result = addNoteUseCase(note).first()

        assertEquals(42L, result)
    }

    @Test
    fun `invoke delegates to repository addNote`() = runTest {
        val note = Note(title = "Title", content = "Body")
        coEvery { noteRepository.addNote(note) } returns 1L

        addNoteUseCase(note).first()

        coVerify(exactly = 1) { noteRepository.addNote(note) }
    }
}
