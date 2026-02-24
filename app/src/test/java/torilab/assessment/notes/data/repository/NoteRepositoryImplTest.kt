package torilab.assessment.notes.data.repository

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import torilab.assessment.notes.data.local.dao.NoteDao
import torilab.assessment.notes.data.local.entity.NoteEntity
import torilab.assessment.notes.domain.model.Note

class NoteRepositoryImplTest {

    private lateinit var noteDao: NoteDao
    private lateinit var repository: NoteRepositoryImpl

    @Before
    fun setUp() {
        noteDao = mockk()
        repository = NoteRepositoryImpl(noteDao)
    }

    @Test
    fun `addNote converts domain model to entity and returns id`() = runTest {
        val entitySlot = slot<NoteEntity>()
        coEvery { noteDao.insertNote(capture(entitySlot)) } returns 7L

        val note = Note(title = "New", content = "Content", createdAt = 100L, updatedAt = 200L)
        val result = repository.addNote(note)

        assertEquals(7L, result)
        assertEquals("New", entitySlot.captured.title)
        assertEquals("Content", entitySlot.captured.content)
    }

    @Test
    fun `addNotes converts all domain models to entities`() = runTest {
        val entitiesSlot = slot<List<NoteEntity>>()
        coEvery { noteDao.insertNotes(capture(entitiesSlot)) } just runs

        val notes = listOf(
            Note(title = "A", content = "1", createdAt = 10L, updatedAt = 10L),
            Note(title = "B", content = "2", createdAt = 20L, updatedAt = 20L)
        )
        repository.addNotes(notes)

        assertEquals(2, entitiesSlot.captured.size)
        assertEquals("A", entitiesSlot.captured[0].title)
        assertEquals("B", entitiesSlot.captured[1].title)
    }

    @Test
    fun `updateNote delegates to dao with mapped entity`() = runTest {
        val entitySlot = slot<NoteEntity>()
        coEvery { noteDao.updateNote(capture(entitySlot)) } just runs

        val note = Note(id = 3L, title = "Updated", content = "Body", createdAt = 50L, updatedAt = 60L)
        repository.updateNote(note)

        assertEquals(3L, entitySlot.captured.id)
        assertEquals("Updated", entitySlot.captured.title)
    }

    @Test
    fun `getNoteById returns mapped domain model when found`() = runTest {
        val entity = NoteEntity(id = 1L, title = "Title", content = "Content", createdAt = 100L, updatedAt = 200L)
        coEvery { noteDao.getNoteById(1L) } returns entity

        val result = repository.getNoteById(1L)

        assertEquals(1L, result?.id)
        assertEquals("Title", result?.title)
        assertEquals("Content", result?.content)
    }

    @Test
    fun `getNoteById returns null when not found`() = runTest {
        coEvery { noteDao.getNoteById(999L) } returns null

        val result = repository.getNoteById(999L)

        assertNull(result)
    }

    @Test
    fun `getNoteCount delegates to dao`() = runTest {
        coEvery { noteDao.getNoteCount() } returns 42

        assertEquals(42, repository.getNoteCount())
        coVerify(exactly = 1) { noteDao.getNoteCount() }
    }

    @Test
    fun `getAllNoteIds delegates to dao`() = runTest {
        coEvery { noteDao.getAllNoteIds() } returns listOf(1L, 2L, 3L)

        val result = repository.getAllNoteIds()

        assertEquals(listOf(1L, 2L, 3L), result)
    }

    @Test
    fun `searchNoteIds delegates to dao with query`() = runTest {
        coEvery { noteDao.searchNoteIds("test") } returns listOf(5L, 10L)

        val result = repository.searchNoteIds("test")

        assertEquals(listOf(5L, 10L), result)
        coVerify(exactly = 1) { noteDao.searchNoteIds("test") }
    }

    @Test
    fun `deleteNoteById delegates to dao`() = runTest {
        coEvery { noteDao.deleteNoteById(1L) } just runs

        repository.deleteNoteById(1L)

        coVerify(exactly = 1) { noteDao.deleteNoteById(1L) }
    }

    @Test
    fun `deleteNotesByIds delegates to dao`() = runTest {
        val ids = listOf(1L, 2L, 3L)
        coEvery { noteDao.deleteNotesByIds(ids) } just runs

        repository.deleteNotesByIds(ids)

        coVerify(exactly = 1) { noteDao.deleteNotesByIds(ids) }
    }
}
