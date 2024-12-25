package ru.example.alp_note.app.presentation.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.example.alp_note.domain.model.NoteModel
import ru.example.alp_note.domain.usecase.DeleteNoteUseCase
import ru.example.alp_note.domain.usecase.GetAllNotesUseCase
import java.sql.Timestamp
import java.time.LocalTime
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) : ViewModel() {

    private val _listNote = MutableLiveData<List<NoteModel>>()
    val listNote: LiveData<List<NoteModel>> = _listNote.switchMap { noteModels ->
        _date.map { selectedDate ->
            noteModels.filter { it.date_start == selectedDate }
        }
    }

    private val _selectedNote = MutableLiveData<NoteModel>()
    val selectedNote: LiveData<NoteModel> = _selectedNote

    private val _date = MutableLiveData<Long>().apply {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        value = calendar.timeInMillis
    }
    val date: LiveData<Long> = _date

    private val _selectHour = MutableLiveData<Int>().apply {
        value = LocalTime.now().hour
    }
    val selectHour: LiveData<Int> = _selectHour

    fun setDateFromCalendar(year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance().apply {
            set(year, month, dayOfMonth, selectHour.value ?: 0, 0, 0 )
            set(Calendar.MILLISECOND,0)
        }.timeInMillis
        _date.value = calendar
    }

    fun selectedTime(position: Int) {
        _selectHour.value = position
        val date = _date.value ?:return
        val calendar = Calendar.getInstance().apply {
            timeInMillis = date
            set(Calendar.HOUR_OF_DAY, selectHour.value ?: 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND,0)
        }.timeInMillis
        _date.value = calendar
    }

    fun selectedNote(note: NoteModel) {
        _selectedNote.value = note
    }

    fun getALl() {
        viewModelScope.launch {
            _listNote.value = getAllNotesUseCase()
        }
    }

    fun deleteNote(note: NoteModel){
        viewModelScope.launch {
            Log.d("MainViewModel", "Deleting note: ${note.name}")
            deleteNoteUseCase(note)
            getALl()
        }
    }
}