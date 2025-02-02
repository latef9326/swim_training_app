package com.example.lab6ex4

import Trainings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Adapter for displaying a list of swimming training sessions in a RecyclerView.
 *
 * @property trainingList The list of training sessions to be displayed.
 * @property onEditClick Callback function invoked when the edit button is clicked.
 * @property onDeleteClick Callback function invoked when the delete button is clicked.
 */
class TrainingAdapter(
    private var trainingList: List<Trainings>,
    private val onEditClick: (Trainings) -> Unit,
    private val onDeleteClick: (Trainings) -> Unit
) : RecyclerView.Adapter<TrainingAdapter.TrainingViewHolder>() {

    /**
     * ViewHolder representing a single training session item in the list.
     *
     * @param itemView The view representing an individual item in the RecyclerView.
     */
    class TrainingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTrainingName: TextView = itemView.findViewById(R.id.tvTrainingName)
        val tvTrainingDate: TextView = itemView.findViewById(R.id.tvTrainingDate)
        val tvCalories: TextView = itemView.findViewById(R.id.tvCalories)
        val btnEdit: Button = itemView.findViewById(R.id.btnEdit)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
    }

    /**
     * Creates a new ViewHolder instance.
     *
     * @param parent The parent view group.
     * @param viewType The view type of the new view.
     * @return A new TrainingViewHolder instance.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_training, parent, false)
        return TrainingViewHolder(view)
    }

    /**
     * Binds data to a ViewHolder.
     *
     * @param holder The ViewHolder to bind data to.
     * @param position The position of the item in the list.
     */
    override fun onBindViewHolder(holder: TrainingViewHolder, position: Int) {
        val training = trainingList[position]

        // Set training details in views
        holder.tvTrainingName.text = "Stroke: ${training.stroke_type}"
        holder.tvTrainingDate.text = "Date: ${training.date}"
        holder.tvCalories.text = "Calories: ${training.calories_burned} kcal"

        // Handle edit and delete button clicks
        holder.btnEdit.setOnClickListener { onEditClick(training) }
        holder.btnDelete.setOnClickListener { onDeleteClick(training) }
    }

    /**
     * Returns the total number of items in the list.
     *
     * @return The number of training sessions.
     */
    override fun getItemCount(): Int = trainingList.size

    /**
     * Updates the adapter's data with a new list of training sessions.
     *
     * @param newList The new list of training sessions.
     */
    fun updateData(newList: List<Trainings>) {
        trainingList = newList
        notifyDataSetChanged()
    }
}



