package torilab.assessment.notes.data.local.mapper

import torilab.assessment.notes.data.local.entity.NoteEntity
import torilab.assessment.notes.domain.model.Note

fun NoteEntity.toDomain(): Note = Note(
    id = id,
    title = title,
    content = content,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun Note.toEntity(): NoteEntity = NoteEntity(
    id = id,
    title = title,
    content = content,
    createdAt = createdAt,
    updatedAt = updatedAt
)
