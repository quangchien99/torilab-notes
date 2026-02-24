package torilab.assessment.notes.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import torilab.assessment.notes.data.local.dao.NoteDao
import torilab.assessment.notes.data.local.entity.NoteEntity

@Database(
    entities = [NoteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ToriNotesDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}
