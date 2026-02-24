package torilab.assessment.notes.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import torilab.assessment.notes.data.local.entity.NoteEntity

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(notes: List<NoteEntity>)

    @Update
    suspend fun updateNote(note: NoteEntity)

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNoteById(id: Long): NoteEntity?

    @Query("SELECT * FROM notes ORDER BY updatedAt DESC")
    fun getAllNotes(): PagingSource<Int, NoteEntity>

    @Query(
        """
        SELECT * FROM notes 
        WHERE title LIKE '%' || :query || '%'
        ORDER BY updatedAt DESC
        """
    )
    fun searchNotes(query: String): PagingSource<Int, NoteEntity>

    @Query("SELECT id FROM notes")
    suspend fun getAllNoteIds(): List<Long>

    @Query(
        """
        SELECT id FROM notes 
        WHERE title LIKE '%' || :query || '%'
        """
    )
    suspend fun searchNoteIds(query: String): List<Long>

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteNoteById(id: Long)

    @Query("DELETE FROM notes WHERE id IN (:ids)")
    suspend fun deleteNotesByIds(ids: List<Long>)
}
