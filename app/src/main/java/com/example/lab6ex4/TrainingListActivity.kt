package com.example.lab6ex4

import Trainings
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TrainingListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var trainingAdapter: TrainingAdapter
    private val trainingList = mutableListOf<Trainings>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training_list)

        // Inicjalizacja RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inicjalizacja Adaptera
        trainingAdapter = TrainingAdapter(
            trainingList,
            onEditClick = { training -> editTraining(training) },
            onDeleteClick = { training -> deleteTraining(training) }
        )
        recyclerView.adapter = trainingAdapter

        // Pobranie danych z Firebase
        fetchTrainingsFromFirebase()
    }

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
                        val training = document.toObject(Trainings::class.java)
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


    private fun editTraining(training: Trainings) {
        // Przejdź do innej aktywności, aby edytować trening (implementacja zależna od Twojej aplikacji)
        Toast.makeText(this, "Edit: ${training.stroke_type}", Toast.LENGTH_SHORT).show()
    }

    private fun deleteTraining(training: Trainings) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .collection("trainings")
                .whereEqualTo("date", training.date) // Filtr dla konkretnego treningu
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        document.reference.delete()
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
        }
    }
}
