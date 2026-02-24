package torilab.assessment.notes.ui.screen.home

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import torilab.assessment.notes.domain.repository.NoteRepository
import torilab.assessment.notes.domain.usecase.DeleteMultipleNotesUseCase
import torilab.assessment.notes.domain.usecase.DeleteNoteUseCase
import torilab.assessment.notes.domain.usecase.GetAllNotesUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getAllNotesUseCase: GetAllNotesUseCase
    private lateinit var deleteNoteUseCase: DeleteNoteUseCase
    private lateinit var deleteMultipleNotesUseCase: DeleteMultipleNotesUseCase
    private lateinit var noteRepository: NoteRepository
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getAllNotesUseCase = mockk()
        deleteNoteUseCase = mockk()
        deleteMultipleNotesUseCase = mockk()
        noteRepository = mockk()
        viewModel = HomeViewModel(
            getAllNotesUseCase,
            deleteNoteUseCase,
            deleteMultipleNotesUseCase,
            noteRepository
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct`() {
        val state = viewModel.currentState
        assertFalse(state.isSelectionMode)
        assertTrue(state.selectedNoteIds.isEmpty())
        assertEquals("", state.searchQuery)
    }

    @Test
    fun `NoteLongPressed enters selection mode with that note`() {
        viewModel.onTriggerEvent(HomeEvent.NoteLongPressed(5L))

        val state = viewModel.currentState
        assertTrue(state.isSelectionMode)
        assertEquals(setOf(5L), state.selectedNoteIds)
    }

    @Test
    fun `ToggleNoteSelection adds note to selection`() {
        viewModel.onTriggerEvent(HomeEvent.NoteLongPressed(1L))
        viewModel.onTriggerEvent(HomeEvent.ToggleNoteSelection(2L))

        assertEquals(setOf(1L, 2L), viewModel.currentState.selectedNoteIds)
    }

    @Test
    fun `ToggleNoteSelection removes already selected note`() {
        viewModel.onTriggerEvent(HomeEvent.NoteLongPressed(1L))
        viewModel.onTriggerEvent(HomeEvent.ToggleNoteSelection(2L))
        viewModel.onTriggerEvent(HomeEvent.ToggleNoteSelection(1L))

        assertEquals(setOf(2L), viewModel.currentState.selectedNoteIds)
    }

    @Test
    fun `deselecting all notes exits selection mode`() {
        viewModel.onTriggerEvent(HomeEvent.NoteLongPressed(1L))
        viewModel.onTriggerEvent(HomeEvent.ToggleNoteSelection(1L))

        assertFalse(viewModel.currentState.isSelectionMode)
        assertTrue(viewModel.currentState.selectedNoteIds.isEmpty())
    }

    @Test
    fun `ExitSelectionMode clears selection and mode`() {
        viewModel.onTriggerEvent(HomeEvent.NoteLongPressed(1L))
        viewModel.onTriggerEvent(HomeEvent.ExitSelectionMode)

        assertFalse(viewModel.currentState.isSelectionMode)
        assertTrue(viewModel.currentState.selectedNoteIds.isEmpty())
    }

    @Test
    fun `DeselectAll clears selected ids`() {
        viewModel.onTriggerEvent(HomeEvent.NoteLongPressed(1L))
        viewModel.onTriggerEvent(HomeEvent.ToggleNoteSelection(2L))
        viewModel.onTriggerEvent(HomeEvent.DeselectAll)

        assertTrue(viewModel.currentState.selectedNoteIds.isEmpty())
    }

    @Test
    fun `SearchQueryChanged updates state`() {
        viewModel.onTriggerEvent(HomeEvent.SearchQueryChanged("hello"))

        assertEquals("hello", viewModel.currentState.searchQuery)
    }

    @Test
    fun `ClearSearch resets query to empty`() {
        viewModel.onTriggerEvent(HomeEvent.SearchQueryChanged("test"))
        viewModel.onTriggerEvent(HomeEvent.ClearSearch)

        assertEquals("", viewModel.currentState.searchQuery)
    }

    @Test
    fun `NoteClicked in selection mode toggles selection`() {
        viewModel.onTriggerEvent(HomeEvent.NoteLongPressed(1L))
        viewModel.onTriggerEvent(HomeEvent.NoteClicked(2L))

        assertEquals(setOf(1L, 2L), viewModel.currentState.selectedNoteIds)
    }

    @Test
    fun `NoteClicked outside selection mode emits NavigateToEdit event`() = runTest {
        val event = async { viewModel.uiEvent.first() }

        viewModel.onTriggerEvent(HomeEvent.NoteClicked(5L))
        advanceUntilIdle()

        val result = event.await()
        assertTrue(result is HomeEvent.NavigateToEdit)
        assertEquals(5L, (result as HomeEvent.NavigateToEdit).noteId)
    }

    @Test
    fun `DeleteNote calls use case and emits NoteDeleted event`() = runTest {
        coEvery { deleteNoteUseCase(10L) } returns flow { emit(Unit) }

        val event = async { viewModel.uiEvent.first() }

        viewModel.onTriggerEvent(HomeEvent.DeleteNote(10L))
        advanceUntilIdle()

        assertTrue(event.await() is HomeEvent.NoteDeleted)
        coVerify(exactly = 1) { deleteNoteUseCase(10L) }
    }

    @Test
    fun `SelectAll fetches all note ids when no search query`() = runTest {
        coEvery { noteRepository.getAllNoteIds() } returns listOf(1L, 2L, 3L)

        viewModel.onTriggerEvent(HomeEvent.NoteLongPressed(1L))
        viewModel.onTriggerEvent(HomeEvent.SelectAll)
        advanceUntilIdle()

        assertEquals(setOf(1L, 2L, 3L), viewModel.currentState.selectedNoteIds)
    }

    @Test
    fun `SelectAll fetches filtered ids when search query present`() = runTest {
        coEvery { noteRepository.searchNoteIds("test") } returns listOf(5L, 10L)

        viewModel.onTriggerEvent(HomeEvent.SearchQueryChanged("test"))
        viewModel.onTriggerEvent(HomeEvent.NoteLongPressed(5L))
        viewModel.onTriggerEvent(HomeEvent.SelectAll)
        advanceUntilIdle()

        assertEquals(setOf(5L, 10L), viewModel.currentState.selectedNoteIds)
    }

    @Test
    fun `DeleteSelectedNotes does nothing when no selection`() = runTest {
        viewModel.onTriggerEvent(HomeEvent.DeleteSelectedNotes)
        advanceUntilIdle()

        coVerify(exactly = 0) { deleteMultipleNotesUseCase(any()) }
    }

    @Test
    fun `DeleteSelectedNotes calls use case and exits selection mode`() = runTest {
        coEvery { deleteMultipleNotesUseCase(any()) } returns flow { emit(Unit) }

        viewModel.onTriggerEvent(HomeEvent.NoteLongPressed(1L))
        viewModel.onTriggerEvent(HomeEvent.ToggleNoteSelection(2L))

        val event = async { viewModel.uiEvent.first() }

        viewModel.onTriggerEvent(HomeEvent.DeleteSelectedNotes)
        advanceUntilIdle()

        val result = event.await()
        assertTrue(result is HomeEvent.NotesDeleted)
        assertEquals(2, (result as HomeEvent.NotesDeleted).count)
        assertFalse(viewModel.currentState.isSelectionMode)
        assertTrue(viewModel.currentState.selectedNoteIds.isEmpty())
    }
}
