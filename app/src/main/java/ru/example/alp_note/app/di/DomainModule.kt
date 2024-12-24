package ru.example.alp_note.app.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.example.alp_note.domain.repository.NoteRepository
import ru.example.alp_note.domain.usecase.GetAllNotesUseCase
import ru.example.alp_note.domain.usecase.InsertNewNoteUseCase
import ru.example.alp_note.domain.usecase.UpdateNoteUseCase

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideGetAllNotesUseCase(noteRepository: NoteRepository): GetAllNotesUseCase {
        return GetAllNotesUseCase(noteRepository)
    }

    @Provides
    fun provideInsertNewNoteUseCase(noteRepository: NoteRepository): InsertNewNoteUseCase {
        return InsertNewNoteUseCase(noteRepository = noteRepository)
    }

    @Provides
    fun provideUpdateNoteUserCase(noteRepository: NoteRepository): UpdateNoteUseCase{
        return UpdateNoteUseCase(noteRepository = noteRepository)
    }
}