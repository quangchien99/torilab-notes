package torilab.assessment.notes.data.local.mapper

import org.junit.Assert.assertEquals
import org.junit.Test
import torilab.assessment.notes.data.local.entity.NoteEntity
import torilab.assessment.notes.domain.model.Note

class NoteMapperTest {

    @Test
    fun `NoteEntity toDomain maps all fields correctly`() {
        val entity = NoteEntity(
            id = 1L,
            title = "Test Title",
            content = "Test Content",
            createdAt = 1000L,
            updatedAt = 2000L
        )

        val note = entity.toDomain()

        assertEquals(1L, note.id)
        assertEquals("Test Title", note.title)
        assertEquals("Test Content", note.content)
        assertEquals(1000L, note.createdAt)
        assertEquals(2000L, note.updatedAt)
    }

    @Test
    fun `Note toEntity maps all fields correctly`() {
        val note = Note(
            id = 5L,
            title = "Note Title",
            content = "Note Content",
            createdAt = 3000L,
            updatedAt = 4000L
        )

        val entity = note.toEntity()

        assertEquals(5L, entity.id)
        assertEquals("Note Title", entity.title)
        assertEquals("Note Content", entity.content)
        assertEquals(3000L, entity.createdAt)
        assertEquals(4000L, entity.updatedAt)
    }

    @Test
    fun `roundtrip entity to domain to entity preserves data`() {
        val original = NoteEntity(
            id = 10L,
            title = "Roundtrip",
            content = "Testing roundtrip",
            createdAt = 5000L,
            updatedAt = 6000L
        )

        val result = original.toDomain().toEntity()

        assertEquals(original, result)
    }

    @Test
    fun `roundtrip domain to entity to domain preserves data`() {
        val original = Note(
            id = 20L,
            title = "Roundtrip Domain",
            content = "Testing domain roundtrip",
            createdAt = 7000L,
            updatedAt = 8000L
        )

        val result = original.toEntity().toDomain()

        assertEquals(original, result)
    }

    @Test
    fun `mapping handles empty strings`() {
        val entity = NoteEntity(
            id = 0L,
            title = "",
            content = "",
            createdAt = 0L,
            updatedAt = 0L
        )

        val note = entity.toDomain()

        assertEquals("", note.title)
        assertEquals("", note.content)
    }
}
