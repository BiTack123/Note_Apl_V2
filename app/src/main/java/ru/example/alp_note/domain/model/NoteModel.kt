package ru.example.alp_note.domain.model


data class NoteModel(
    var id: Int,
    var date_start: Long,
    var date_finish: Long,
    var name: String,
    var description: String
)
