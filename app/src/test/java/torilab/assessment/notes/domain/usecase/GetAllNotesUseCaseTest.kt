package torilab.assessment.notes.domain.usecase

import androidx.paging.PagingData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Test
import torilab.assessment.notes.domain.model.Note
import torilab.assessment.notes.domain.repository.NoteRepository

class GetAllNotesUseCaseTest {

    private lateinit var noteRepository: NoteRepository
    private lateinit var getAllNotesUseCase: GetAllNotesUseCase

    @Before
    fun setUp() {
        noteRepository = mockk()
        getAllNotesUseCase = GetAllNotesUseCase(noteRepository)
    }

    @Test
    fun `invoke with blank query calls getAllNotes`() {
        every { noteRepository.getAllNotes() } returns flowOf(PagingData.empty())

        getAllNotesUseCase("")

        verify(exactly = 1) { noteRepository.getAllNotes() }
        verify(exactly = 0) { noteRepository.searchNotes(any()) }
    }

    @Test
    fun `invoke with no arguments calls getAllNotes`() {
        every { noteRepository.getAllNotes() } returns flowOf(PagingData.empty())

        getAllNotesUseCase()

        verify(exactly = 1) { noteRepository.getAllNotes() }
    }

    @Test
    fun `invoke with non-blank query calls searchNotes`() {
        every { noteRepository.searchNotes("test") } returns flowOf(PagingData.empty())

        getAllNotesUseCase("test")

        verify(exactly = 1) { noteRepository.searchNotes("test") }
        verify(exactly = 0) { noteRepository.getAllNotes() }
    }

    @Test
    fun `invoke trims query before searching`() {
        every { noteRepository.searchNotes("hello") } returns flowOf(PagingData.empty())

        getAllNotesUseCase("  hello  ")

        verify(exactly = 1) { noteRepository.searchNotes("hello") }
    }

    @Test
    fun `invoke with whitespace-only query calls getAllNotes`() {
        every { noteRepository.getAllNotes() } returns flowOf(PagingData.empty())

        getAllNotesUseCase("   ")

        verify(exactly = 1) { noteRepository.getAllNotes() }
    }
}
