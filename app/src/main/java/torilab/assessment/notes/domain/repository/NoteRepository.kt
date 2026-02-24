package torilab.assessment.notes.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import torilab.assessment.notes.domain.model.Note

interface NoteRepository {
    suspend fun addNote(note: Note): Long
    suspend fun updateNote(note: Note)
    suspend fun getNoteById(id: Long): Note?
    fun getAllNotes(): Flow<PagingData<Note>>
    suspend fun deleteNoteById(id: Long)
}
