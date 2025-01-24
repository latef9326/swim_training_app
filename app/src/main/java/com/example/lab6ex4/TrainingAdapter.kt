package com.example.lab6ex4

import Trainings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TrainingAdapter(
    private var trainingList: List<Trainings>,
    private val onEditClick: (Trainings) -> Unit,
    private val onDeleteClick: (Trainings) -> Unit
) : RecyclerView.Adapter<TrainingAdapter.TrainingViewHolder>() {

    // ViewHolder dla pojedynczego elementu
    class TrainingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTrainingName: TextView = itemView.findViewById(R.id.tvTrainingName)
        val tvTrainingDate: TextView = itemView.findViewById(R.id.tvTrainingDate)
        val tvCalories: TextView = itemView.findViewById(R.id.tvCalories)
        val btnEdit: Button = itemView.findViewById(R.id.btnEdit)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_training, parent, false)
        return TrainingViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrainingViewHolder, position: Int) {
        val training = trainingList[position]

        // Ustawiamy dane treningu w widokach
        holder.tvTrainingName.text = "Stroke: ${training.stroke_type}"
        holder.tvTrainingDate.text = "Date: ${training.date}"
        holder.tvCalories.text = "Calories: ${training.calories} kcal"

        // Obsługa przycisków "Edit" i "Delete"
        holder.btnEdit.setOnClickListener { onEditClick(training) }
        holder.btnDelete.setOnClickListener { onDeleteClick(training) }
    }

    override fun getItemCount(): Int = trainingList.size

    // Metoda do aktualizacji danych w liście
    fun updateData(newList: List<Trainings>) {
        trainingList = newList
        notifyDataSetChanged()
    }
}


