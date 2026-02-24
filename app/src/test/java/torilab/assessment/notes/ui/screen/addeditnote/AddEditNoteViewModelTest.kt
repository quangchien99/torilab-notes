package torilab.assessment.notes.ui.screen.addeditnote

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
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
import torilab.assessment.notes.domain.model.Note
import torilab.assessment.notes.domain.usecase.AddNoteUseCase
import torilab.assessment.notes.domain.usecase.DeleteNoteUseCase
import torilab.assessment.notes.domain.usecase.GetNoteByIdUseCase
import torilab.assessment.notes.domain.usecase.UpdateNoteUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class AddEditNoteViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var addNoteUseCase: AddNoteUseCase
    private lateinit var updateNoteUseCase: UpdateNoteUseCase
    private lateinit var getNoteByIdUseCase: GetNoteByIdUseCase
    private lateinit var deleteNoteUseCase: DeleteNoteUseCase
    private lateinit var viewModel: AddEditNoteViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        addNoteUseCase = mockk()
        updateNoteUseCase = mockk()
        getNoteByIdUseCase = mockk()
        deleteNoteUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModelForNewNote(): AddEditNoteViewModel {
        val savedStateHandle = androidx.lifecycle.SavedStateHandle()
        return AddEditNoteViewModel(
            savedStateHandle,
            addNoteUseCase,
            updateNoteUseCase,
            getNoteByIdUseCase,
            deleteNoteUseCase
        )
    }

    @Test
    fun `initial state for new note is correct`() {
        viewModel = createViewModelForNewNote()

        assertEquals("", viewModel.currentState.title)
        assertEquals("", viewModel.currentState.content)
        assertFalse(viewModel.currentState.isEditMode)
        assertEquals(null, viewModel.currentState.noteId)
        assertFalse(viewModel.currentState.isLoading)
        assertFalse(viewModel.currentState.isSaving)
    }

    @Test
    fun `TitleChanged updates title in state`() {
        viewModel = createViewModelForNewNote()

        viewModel.onTriggerEvent(AddEditNoteEvent.TitleChanged("New Title"))

        assertEquals("New Title", viewModel.currentState.title)
    }

    @Test
    fun `ContentChanged updates content in state`() {
        viewModel = createViewModelForNewNote()

        viewModel.onTriggerEvent(AddEditNoteEvent.ContentChanged("New Content"))

        assertEquals("New Content", viewModel.currentState.content)
    }

    @Test
    fun `SaveNote with blank title emits ShowError event`() = runTest {
        viewModel = createViewModelForNewNote()
        viewModel.onTriggerEvent(AddEditNoteEvent.TitleChanged(""))
        viewModel.onTriggerEvent(AddEditNoteEvent.ContentChanged("Some content"))

        val event = async { viewModel.uiEvent.first() }

        viewModel.onTriggerEvent(AddEditNoteEvent.SaveNote)
        advanceUntilIdle()

        val result = event.await()
        assertTrue(result is AddEditNoteEvent.ShowError)
        assertEquals("Title cannot be empty", (result as AddEditNoteEvent.ShowError).message)
    }

    @Test
    fun `SaveNote for new note calls addNoteUseCase`() = runTest {
        val noteSlot = slot<Note>()
        coEvery { addNoteUseCase(capture(noteSlot)) } returns flow { emit(1L) }

        viewModel = createViewModelForNewNote()
        viewModel.onTriggerEvent(AddEditNoteEvent.TitleChanged("My Note"))
        viewModel.onTriggerEvent(AddEditNoteEvent.ContentChanged("My Content"))

        val event = async { viewModel.uiEvent.first() }

        viewModel.onTriggerEvent(AddEditNoteEvent.SaveNote)
        advanceUntilIdle()

        assertTrue(event.await() is AddEditNoteEvent.NoteSaved)
        assertEquals("My Note", noteSlot.captured.title)
        assertEquals("My Content", noteSlot.captured.content)
    }

    @Test
    fun `SaveNote trims title and content`() = runTest {
        val noteSlot = slot<Note>()
        coEvery { addNoteUseCase(capture(noteSlot)) } returns flow { emit(1L) }

        viewModel = createViewModelForNewNote()
        viewModel.onTriggerEvent(AddEditNoteEvent.TitleChanged("  Padded Title  "))
        viewModel.onTriggerEvent(AddEditNoteEvent.ContentChanged("  Padded Content  "))

        viewModel.onTriggerEvent(AddEditNoteEvent.SaveNote)
        advanceUntilIdle()

        assertEquals("Padded Title", noteSlot.captured.title)
        assertEquals("Padded Content", noteSlot.captured.content)
    }

    @Test
    fun `DeleteNote does nothing when not in edit mode`() = runTest {
        viewModel = createViewModelForNewNote()

        viewModel.onTriggerEvent(AddEditNoteEvent.DeleteNote)
        advanceUntilIdle()

        coVerify(exactly = 0) { deleteNoteUseCase(any()) }
    }

    @Test
    fun `multiple title changes only keeps the latest`() {
        viewModel = createViewModelForNewNote()

        viewModel.onTriggerEvent(AddEditNoteEvent.TitleChanged("First"))
        viewModel.onTriggerEvent(AddEditNoteEvent.TitleChanged("Second"))
        viewModel.onTriggerEvent(AddEditNoteEvent.TitleChanged("Third"))

        assertEquals("Third", viewModel.currentState.title)
    }
}
