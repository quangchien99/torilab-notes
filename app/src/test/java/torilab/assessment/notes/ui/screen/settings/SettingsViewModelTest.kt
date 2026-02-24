package torilab.assessment.notes.ui.screen.settings

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
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

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var noteRepository: NoteRepository
    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        noteRepository = mockk()
        viewModel = SettingsViewModel(noteRepository, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct`() {
        assertFalse(viewModel.currentState.isGenerating)
        assertEquals(0, viewModel.currentState.generatedCount)
    }

    @Test
    fun `GenerateBulkNotes sets isGenerating to true`() = runTest {
        coEvery { noteRepository.getNoteCount() } returns 0
        coEvery { noteRepository.addNotes(any()) } coAnswers { delay(100) }

        viewModel.onTriggerEvent(SettingsEvent.GenerateBulkNotes)
        testDispatcher.scheduler.advanceTimeBy(1)

        assertTrue(viewModel.currentState.isGenerating)
    }

    @Test
    fun `GenerateBulkNotes completes and resets state`() = runTest {
        coEvery { noteRepository.getNoteCount() } returns 0
        coEvery { noteRepository.addNotes(any()) } just runs

        val event = async { viewModel.uiEvent.first() }

        viewModel.onTriggerEvent(SettingsEvent.GenerateBulkNotes)
        advanceUntilIdle()

        assertTrue(event.await() is SettingsEvent.BulkNotesGenerated)
        assertFalse(viewModel.currentState.isGenerating)
    }

    @Test
    fun `GenerateBulkNotes inserts 10 batches of 100`() = runTest {
        coEvery { noteRepository.getNoteCount() } returns 0
        coEvery { noteRepository.addNotes(any()) } just runs

        viewModel.onTriggerEvent(SettingsEvent.GenerateBulkNotes)
        advanceUntilIdle()

        coVerify(exactly = 10) { noteRepository.addNotes(any()) }
    }

    @Test
    fun `GenerateBulkNotes uses existing count as offset`() = runTest {
        coEvery { noteRepository.getNoteCount() } returns 50
        coEvery { noteRepository.addNotes(any()) } just runs

        viewModel.onTriggerEvent(SettingsEvent.GenerateBulkNotes)
        advanceUntilIdle()

        coVerify { noteRepository.getNoteCount() }
    }

    @Test
    fun `GenerateBulkNotes ignores duplicate trigger while generating`() = runTest {
        coEvery { noteRepository.getNoteCount() } returns 0
        coEvery { noteRepository.addNotes(any()) } coAnswers { delay(100) }

        viewModel.onTriggerEvent(SettingsEvent.GenerateBulkNotes)
        testDispatcher.scheduler.advanceTimeBy(1)

        assertTrue(viewModel.currentState.isGenerating)

        viewModel.onTriggerEvent(SettingsEvent.GenerateBulkNotes)
        advanceUntilIdle()

        coVerify(exactly = 10) { noteRepository.addNotes(any()) }
    }
}
