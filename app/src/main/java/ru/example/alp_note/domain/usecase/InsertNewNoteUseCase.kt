package ru.example.alp_note.domain.usecase

import ru.example.alp_note.domain.model.NoteModel
import ru.example.alp_note.domain.repository.NoteRepository

class InsertNewNoteUseCase(private val noteRepository: NoteRepository){
    suspend operator fun invoke(note: NoteModel) = noteRepository.insertNote(note)
}