package com.example.lab6ex4

import Trainings
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Activity that displays a list of swimming training sessions retrieved from Firebase.
 * Allows users to edit, delete, and sort their training sessions.
 */
class TrainingListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var trainingAdapter: TrainingAdapter
    private val trainingList = mutableListOf<Trainings>()

    /**
     * Called when the activity is starting. Initializes UI elements,
     * sets up RecyclerView, and fetches training data from Firebase.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down, this Bundle contains the most recent data.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training_list)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        trainingAdapter = TrainingAdapter(
            trainingList,
            onEditClick = { training -> editTraining(training) },
            onDeleteClick = { training -> deleteTraining(training) }
        )
        recyclerView.adapter = trainingAdapter

        // Set up sorting buttons
        findViewById<Button>(R.id.btnSortByDate).setOnClickListener { sortTrainingsByDate() }
        findViewById<Button>(R.id.btnSortByDistance).setOnClickListener { sortTrainingsByDistance() }
        findViewById<Button>(R.id.btnSortByCalories).setOnClickListener { sortTrainingsByCalories() }

        fetchTrainingsFromFirebase()
    }

    /**
     * Fetches training sessions from Firebase Firestore and updates the RecyclerView.
     */
    private fun fetchTrainingsFromFirebase() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .collection("trainings")
                .get()
                .addOnSuccessListener { documents ->
                    trainingList.clear()
                    for (document in documents) {
                        val training = document.toObject(Trainings::class.java).copy(id = document.id)
                        trainingList.add(training)
                    }
                    trainingAdapter.updateData(trainingList)
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Starts the EditTrainingActivity to allow the user to edit a selected training session.
     *
     * @param training The training session to be edited.
     */
    private fun editTraining(training: Trainings) {
        val intent = Intent(this, EditTrainingActivity::class.java).apply {
            putExtra("training", training)
        }
        startActivityForResult(intent, 1)
    }

    /**
     * Handles the result from EditTrainingActivity and refreshes the training list.
     *
     * @param requestCode The request code passed when starting the activity.
     * @param resultCode The result code returned from the activity.
     * @param data The intent data returned from the activity.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            fetchTrainingsFromFirebase()
        }
    }

    /**
     * Deletes a training session from Firebase and updates the RecyclerView.
     *
     * @param training The training session to be deleted.
     */
    private fun deleteTraining(training: Trainings) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null && training.id.isNotEmpty()) {
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .collection("trainings")
                .document(training.id)
                .delete()
                .addOnSuccessListener {
                    trainingList.remove(training)
                    trainingAdapter.updateData(trainingList)
                    Toast.makeText(this, "Training deleted.", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    /**
     * Sorts the training sessions by date in ascending order.
     */
    private fun sortTrainingsByDate() {
        trainingList.sortBy { it.date }
        trainingAdapter.updateData(trainingList)
    }

    /**
     * Sorts the training sessions by distance in descending order.
     */
    private fun sortTrainingsByDistance() {
        trainingList.sortByDescending { it.distance_meters }
        trainingAdapter.updateData(trainingList)
    }

    /**
     * Sorts the training sessions by calories burned in descending order.
     */
    private fun sortTrainingsByCalories() {
        trainingList.sortByDescending { it.calories_burned }
        trainingAdapter.updateData(trainingList)
    }
}

//private fun fetchTrainingsFromFirebase() {
//    val userId = FirebaseAuth.getInstance().currentUser?.uid
//    if (userId != null) {
//        FirebaseFirestore.getInstance()
//            .collection("users")
//            .document(userId)
//            .collection("trainings")
//            .get()
//            .addOnSuccessListener { documents ->
//                trainingList.clear()
//                for (document in documents) {
//                    val training = document.toObject(Trainings::class.java)
//                    trainingList.add(training)
//                }
//                trainingAdapter.updateData(trainingList)
//            }
//            .addOnFailureListener { e ->
//                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
//            }
//    } else {
//        Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show()
//    }
//}