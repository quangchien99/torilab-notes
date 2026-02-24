package torilab.assessment.notes.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import torilab.assessment.notes.data.local.dao.NoteDao
import torilab.assessment.notes.data.local.database.ToriNotesDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideToriNotesDatabase(
        @ApplicationContext context: Context
    ): ToriNotesDatabase {
        return Room.databaseBuilder(
            context,
            ToriNotesDatabase::class.java,
            "tori_notes_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteDao(database: ToriNotesDatabase): NoteDao {
        return database.noteDao()
    }
}
