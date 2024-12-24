package ru.example.alp_note.app.presentation.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.example.alp_note.domain.model.NoteModel
import ru.example.alp_note.domain.usecase.InsertNewNoteUseCase
import ru.example.alp_note.domain.usecase.UpdateNoteUseCase
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class SecondViewModel @Inject constructor(
    private val insertNewNoteUseCase: InsertNewNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase
) : ViewModel() {

    private var note = NoteModel(
        id = 0,
        date_start = 0L,
        date_finish = 0L,
        name = "",
        description = ""
    )
    private val _date = MutableLiveData<Long>()
    val date: LiveData<Long> = _date

    private val _time = MutableLiveData<Int>()
    val time: LiveData<Int> = _time

    private val _header = MutableLiveData<String>()
    val header: LiveData<String> = _header

    private val _description = MutableLiveData<String>()
    val description: LiveData<String> = _description

    fun setTime(time: Int) {
        _time.value = time
        setFullDate()
    }

    fun setDate(date: Long) {
        _date.value = date
        setFullDate()
    }

    fun setHeader(header: String) {
        _header.value = header
        note = note.copy(name = header)
    }

    fun setDescription(description: String) {
        _description.value = description
        note = note.copy(description = description)
    }

    private fun setFullDate() {
        val timeStart = _time.value ?: return
        val date = _date.value ?: return

        val timeFinish = (timeStart + 1) % 24

        val dateStart = Calendar.getInstance().apply {
            timeInMillis = date
            set(Calendar.HOUR_OF_DAY, timeStart)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val dateFinish = Calendar.getInstance().apply {
            timeInMillis = date
            set(Calendar.HOUR_OF_DAY, timeFinish)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        note = note.copy(
            date_start = dateStart,
            date_finish = dateFinish
        )
    }

    fun setNote(/*_note: NoteModel*/
                id: Int,
                date_start: Long,
                date_finish: Long,
                name: String,
                description: String
    ) {
        note = note.copy(
            id = id,
            date_start = date_start,
            date_finish = date_finish,
            name = name,
            description = description
        )
        setTime(
            LocalDateTime.ofInstant(
                Instant.ofEpochMilli(note.date_start),
                ZoneId.systemDefault()
            ).hour
        )
        setDate(note.date_start)
        _header.value = name
        _description.value = description
        //note = _note.copy()
    }

    fun insertNewNote() {
        viewModelScope.launch {
            insertNewNoteUseCase(note)
        }
    }

    fun updateNote() {
        viewModelScope.launch {
            updateNoteUseCase(note)
        }
    }
}
