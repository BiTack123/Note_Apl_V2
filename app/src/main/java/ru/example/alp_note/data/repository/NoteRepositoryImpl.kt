package ru.example.alp_note.data.repository

import ru.example.alp_note.data.local.NoteDao
import ru.example.alp_note.data.local.NoteEntity
import ru.example.alp_note.domain.model.NoteModel
import ru.example.alp_note.domain.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NoteRepositoryImpl (private val noteDao: NoteDao) : NoteRepository {
    override suspend fun getAllNotes(): List<NoteModel> {
        return withContext(Dispatchers.IO) {
            noteDao.getAll().map {
                it.toModel()
            }
        }
    }

    override suspend fun insertNote(note: NoteModel) {
        withContext(Dispatchers.IO) {
            noteDao.insertNote(note.toEntity())
        }
    }

    override suspend fun updateNote(note: NoteModel) {
        withContext(Dispatchers.IO) {
            noteDao.updateNote(note.toEntity())
        }
    }

    override suspend fun deleteNote(note: NoteModel) {
        withContext(Dispatchers.IO) {
            noteDao.deleteNote(note.toEntity())
        }
    }

    private fun NoteModel.toEntity() = NoteEntity(id, date_start, date_finish, name, description)
    private fun NoteEntity.toModel() = NoteModel(id, date_start, date_finish, name, description)

}
