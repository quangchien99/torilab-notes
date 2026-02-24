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
import torilab.assessment.notes.domain.model.Note
import torilab.assessment.notes.domain.repository.NoteRepository

class UpdateNoteUseCaseTest {

    private lateinit var noteRepository: NoteRepository
    private lateinit var updateNoteUseCase: UpdateNoteUseCase

    @Before
    fun setUp() {
        noteRepository = mockk()
        updateNoteUseCase = UpdateNoteUseCase(noteRepository)
    }

    @Test
    fun `invoke emits Unit after successful update`() = runTest {
        val note = Note(id = 1L, title = "Updated", content = "Updated Content")
        coEvery { noteRepository.updateNote(note) } just runs

        val result = updateNoteUseCase(note).first()

        assertEquals(Unit, result)
    }

    @Test
    fun `invoke delegates to repository updateNote`() = runTest {
        val note = Note(id = 5L, title = "Title", content = "Body")
        coEvery { noteRepository.updateNote(note) } just runs

        updateNoteUseCase(note).first()

        coVerify(exactly = 1) { noteRepository.updateNote(note) }
    }
}
