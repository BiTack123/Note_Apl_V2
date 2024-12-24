package ru.example.alp_note.app.presentation.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.example.alp_note.databinding.IconTimeItemBinding

class MainAdapterTime(
    private val times: List<Int>,
    private var selectedPosition: Int,
    private val onTimeSelected: (Int) -> Unit
): RecyclerView.Adapter<MainAdapterTime.TimeViewHolder>() {

    class TimeViewHolder(val binding: IconTimeItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder {
        val binding = IconTimeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TimeViewHolder(binding)
    }

    override fun getItemCount(): Int = times.size

    override fun onBindViewHolder(holder: TimeViewHolder, position: Int) {
        val time = times[position]
        with(holder.binding) {
            tvTime.text = time.toString()
            tvTime.isSelected = (selectedPosition == position)

            root.setOnClickListener {
                onTimeSelected(position)
            }
        }
    }

    fun updateSelectedPosition(position: Int) {
        selectedPosition = position
        notifyDataSetChanged() // Обновляем только выбранную позицию
    }

}