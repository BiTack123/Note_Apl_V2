package ru.example.alp_note.domain.repository

import ru.example.alp_note.domain.model.NoteModel

interface NoteRepository {

    suspend fun getAllNotes(): List<NoteModel>

    suspend fun insertNote(note: NoteModel)

    suspend fun updateNote(note: NoteModel)

    suspend fun deleteNote(note: NoteModel)
}