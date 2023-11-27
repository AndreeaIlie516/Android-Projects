package com.android.noteapp.di

import android.app.Application
import androidx.room.Room
import com.android.noteapp.feature_note.data.data_source.NoteDatabase
import com.android.noteapp.feature_note.data.repository.NoteRepositoryImpl
import com.android.noteapp.feature_note.domain.repository.NoteRepository
import com.android.noteapp.feature_note.domain.use_case.AddNoteUseCase
import com.android.noteapp.feature_note.domain.use_case.DeleteNoteUseCase
import com.android.noteapp.feature_note.domain.use_case.GetNoteUseCase
import com.android.noteapp.feature_note.domain.use_case.GetNotesUseCase
import com.android.noteapp.feature_note.domain.use_case.NoteUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): NoteDatabase {
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    fun prvideNoteRepository(db: NoteDatabase): NoteRepository {
        return NoteRepositoryImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            getNotesUseCase = GetNotesUseCase(repository),
            deleteNoteUseCase = DeleteNoteUseCase(repository),
            addNoteUseCase = AddNoteUseCase(repository),
            getNoteUseCase = GetNoteUseCase(repository)
        )
    }
}