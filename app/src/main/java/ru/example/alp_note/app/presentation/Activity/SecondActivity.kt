package ru.example.alp_note.app.presentation.Activity

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import ru.example.alp_note.app.presentation.ViewModel.SecondViewModel
import ru.example.alp_note.databinding.ActivitySecondBinding
import ru.example.alp_note.domain.model.NoteModel
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class SecondActivity : AppCompatActivity() {

    private val secondViewModel: SecondViewModel by viewModels()
    private lateinit var binding: ActivitySecondBinding

        private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dateTextView = binding.dateTextView
        val timeTextView = binding.timeTextView
        val etHeader = binding.etHeader
        val tvHeader = binding.tvHeader
        val etDescription = binding.etDescription
        val btnClose = binding.btnCloseAct

        val isEditing = intent.getBooleanExtra("isEditing", false)
        val name = intent.getStringExtra("name") ?: ""
        val description = intent.getStringExtra("description") ?: ""

        etHeader.setText(name)
        etDescription.setText(description)

        secondViewModel.setNote(
            intent.getIntExtra("id", 0),
            intent.getLongExtra("date_start", 0L),
            intent.getLongExtra("date_finish", 0L),
            name,
            description
        )

        secondViewModel.date.observe(this) { date ->
            date?.let {
                dateTextView.text = "Дата: ${dateFormat.format(it)}"
            }
        }
        dateTextView.setOnClickListener {
            // Создаем экземпляр
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Выберите дату")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            // Показываем окно
            datePicker.show(supportFragmentManager, "DATE_PICKER")

            datePicker.addOnPositiveButtonClickListener { selection ->
                secondViewModel.setDate(selection)
            }
        }

        secondViewModel.time.observe(this, Observer { time ->
            time?.let {
                timeTextView.text = String.format(
                    "Время: %02d:00 - %02d:00", it, (it + 1) % 24
                )
            }
        })

        timeTextView.setOnClickListener {
            val picker =
                MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).setHour(12)
                    .setMinute(0).setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                    .setTitleText("Выберите время").build()
            picker.show(supportFragmentManager, "TIME_PICKER")

            picker.addOnPositiveButtonClickListener {
                secondViewModel.setTime(picker.hour)
                picker.dismiss()
            }
        }

        secondViewModel.header.observe(this, Observer { header ->
            header?.let {
                //etHeader.setText(it)
                tvHeader.text = it
            }
        })

        secondViewModel.description.observe(this, Observer { description ->
            description?.let {
                //etDescription.setText(it)
            }
        })

        etHeader.doAfterTextChanged { text ->
            secondViewModel.setHeader(text.toString())
        }


        etDescription.doAfterTextChanged { text ->
            secondViewModel.setDescription(text.toString())
        }

        btnClose.setOnClickListener {
            if (isEditing){
                secondViewModel.updateNote()
            } else {
                if(etDescription.text.toString().isNotBlank() || etHeader.text.toString().isNotBlank()){
                    secondViewModel.insertNewNote()
                }
            }
            setResult(RESULT_OK)
            finish()
        }
    }
}
