package torilab.assessment.notes.data.repository

import torilab.assessment.notes.data.local.dao.NoteDao
import torilab.assessment.notes.data.local.mapper.toDomain
import torilab.assessment.notes.data.local.mapper.toEntity
import torilab.assessment.notes.domain.model.Note
import torilab.assessment.notes.domain.repository.NoteRepository
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao
) : NoteRepository {

    override suspend fun addNote(note: Note): Long {
        return noteDao.insertNote(note.toEntity())
    }

    override suspend fun updateNote(note: Note) {
        noteDao.updateNote(note.toEntity())
    }

    override suspend fun getNoteById(id: Long): Note? {
        return noteDao.getNoteById(id)?.toDomain()
    }
}
