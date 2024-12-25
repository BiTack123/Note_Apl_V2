package ru.example.alp_note.app.presentation.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.example.alp_note.databinding.NoteItemBinding
import ru.example.alp_note.domain.model.NoteModel
import java.text.SimpleDateFormat
import java.util.Locale

class MainAdapterNotes (
    private val listNote: MutableList<NoteModel>,
    private val onNoteSelected: (NoteModel) -> Unit,
    private val onNoteDelete: (NoteModel) -> Unit
): RecyclerView.Adapter<MainAdapterNotes.NotesViewHolder>(){
    class NotesViewHolder(val binding: NoteItemBinding): RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotesViewHolder {
        val binding = NoteItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NotesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val note = listNote[position]
        with(holder.binding){
            tvHeader.text = note.name
            tvDescription.text = note.description
            tvDateTime.text = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(note.date_start)
            btnDelete.setOnClickListener{
                Log.d("MainAdapterNotes", "Delete button clicked for note: ${note.name}")
                onNoteDelete(note)
            }
            llFirsZone.setOnClickListener {
                Log.d("MainAdapterNotes","Нажата область")
                onNoteSelected(note)
            }
        }
    }

    override fun getItemCount(): Int = listNote.size

    fun updateData(newNotes: List<NoteModel>) {
        listNote.clear()
        listNote.addAll(newNotes)
        notifyDataSetChanged()
    }

}