package torilab.assessment.notes.domain.repository

import torilab.assessment.notes.domain.model.Note

interface NoteRepository {
    suspend fun addNote(note: Note): Long
    suspend fun updateNote(note: Note)
    suspend fun getNoteById(id: Long): Note?
}
