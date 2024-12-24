package ru.example.alp_note.app.presentation.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ru.example.alp_note.app.presentation.Adapter.MainAdapterNotes
import ru.example.alp_note.app.presentation.Adapter.MainAdapterTime
import ru.example.alp_note.app.presentation.ViewModel.MainViewModel
import ru.example.alp_note.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var notesAdapter: MainAdapterNotes
    private lateinit var timeAdapter: MainAdapterTime

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel._listNote.observe(this, Observer { noteList ->
            // Здесь мы получаем актуальные данные, так как Observer срабатывает при обновлении LiveData
            Log.v("ListNote_", "Updated list: $noteList")
        })

        mainViewModel.getALl()

        //AdapterTime
        val timeRecycleView = binding.rvTime
        val adapterTime = MainAdapterTime(
            (0..23).toList(),
            mainViewModel.selectHour.value ?:0
        ) { selectedPosition ->
            mainViewModel.selectedTime(selectedPosition)
        }
        timeRecycleView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        timeRecycleView.adapter = adapterTime

        mainViewModel.selectHour.observe(this, Observer { selecthour ->
            adapterTime.updateSelectedPosition(selecthour)
            binding.rvTime.scrollToPosition(mainViewModel.selectHour.value ?: 0)
        })

        //AdapterNotes
        val notesRecyclerView = binding.rvNotes
        val adapterNotes = MainAdapterNotes (
            mutableListOf()
        ){ selectedNote ->
            mainViewModel.selectedNote(selectedNote)
        }
        notesRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        notesRecyclerView.adapter = adapterNotes

        mainViewModel.listNote.observe(this, Observer { notes ->
            adapterNotes.updateData(notes)
            Log.v("ListNote", "Updated list: $notes , ${mainViewModel.date.value!!}")
        })

        val calendarView = binding.calendarView
        mainViewModel.date.observe(this, Observer { date ->
            date?.let {
                calendarView.date = it
            }
        })

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            mainViewModel.setDateFromCalendar(year, month, dayOfMonth)
        }

        val secondActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                mainViewModel.getALl() // Обновить список заметок
            }
        }

        val btnCreateNewNote = binding.imbtnCreateNew
        btnCreateNewNote.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("isEditing", false) // Новый режим (не редактирование)
            intent.putExtra("id", 0)
            intent.putExtra("date_start", mainViewModel.date.value!!)
            intent.putExtra("date_finish", mainViewModel.date.value!!)
            intent.putExtra("name", "")
            intent.putExtra("description", "")
            secondActivityLauncher.launch(intent) // Запуск через launcher
        }

        mainViewModel.selectedNote.observe(this, Observer { note ->
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("isEditing", true) // Режим редактирования
            intent.putExtra("id", note.id)
            intent.putExtra("date_start", note.date_start)
            intent.putExtra("date_finish", note.date_finish)
            intent.putExtra("name", note.name)
            intent.putExtra("description", note.description)
            secondActivityLauncher.launch(intent) // Запуск через launcher
        })
    }
}
