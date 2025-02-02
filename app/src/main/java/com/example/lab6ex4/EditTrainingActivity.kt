package com.example.lab6ex4

import Trainings
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Activity for editing an existing training session.
 *
 * This activity allows the user to modify the details of a training session and save the changes
 * to Firestore. It also calculates updated health metrics such as speed, calories burned, and
 * heart rate zone based on the modified data.
 */
class EditTrainingActivity : AppCompatActivity() {

    private lateinit var etDate: EditText
    private lateinit var etDuration: EditText
    private lateinit var etDistance: EditText
    private lateinit var etStrokeType: EditText
    private lateinit var etEffortLevel: EditText
    private lateinit var etHeartRate: EditText
    private lateinit var btnSave: Button
    private var userWeight: Float = 0f
    private lateinit var training: Trainings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_training)

        training = intent.getSerializableExtra("training") as Trainings
        initViews()
        loadUserWeight()
        setupSaveButton()
    }

    /**
     * Initializes the views and populates them with the current training data.
     */
    private fun initViews() {
        etDate = findViewById(R.id.etDate)
        etDuration = findViewById(R.id.etDuration)
        etDistance = findViewById(R.id.etDistance)
        etStrokeType = findViewById(R.id.etStrokeType)
        etEffortLevel = findViewById(R.id.etEffortLevel)
        etHeartRate = findViewById(R.id.etHeartRate)
        btnSave = findViewById(R.id.btnSave)

        etDate.setText(training.date)
        etDuration.setText(training.duration_minutes.toString())
        etDistance.setText(training.distance_meters.toString())
        etStrokeType.setText(training.stroke_type)
        etEffortLevel.setText(training.effort_level.toString())
        etHeartRate.setText(training.heart_rate.toString())
    }

    /**
     * Sets up the save button click listener to validate and update the training data.
     */
    private fun setupSaveButton() {
        btnSave.setOnClickListener {
            val updatedTraining = validateAndUpdateTraining()
            updatedTraining?.let {
                updateTrainingInFirestore(it)
            }
        }
    }

    /**
     * Loads the user's weight from Firestore.
     *
     * This method retrieves the user's weight from the "health" field in the Firestore document
     * associated with the current user. If the weight is not set or the user is not logged in,
     * appropriate messages are displayed.
     */
    private fun loadUserWeight() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener { document ->
                    val health = document.get("health") as? Map<*, *>
                    userWeight = (health?.get("weight") as? Number)?.toFloat() ?: 0f
                    if (userWeight <= 0f) {
                        Toast.makeText(this, "Configure health data first", Toast.LENGTH_LONG).show()
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error loading weight", e)
                    Toast.makeText(this, "Error loading data", Toast.LENGTH_SHORT).show()
                }
        }
    }

    /**
     * Validates the input fields for the training data.
     *
     * @param date The date of the training in "dd/MM/yyyy" format.
     * @param duration The duration of the training in minutes.
     * @param distance The distance covered in meters.
     * @param strokeType The type of swimming stroke.
     * @param effortLevel The perceived effort level (1-10).
     * @param heartRate The average heart rate during the training.
     * @return `true` if all inputs are valid, `false` otherwise.
     */
    private fun validateInputs(
        date: String,
        duration: Int,
        distance: Int,
        strokeType: String,
        effortLevel: Int,
        heartRate: Int
    ): Boolean {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dateFormat.isLenient = false
        return try {
            dateFormat.parse(date)
            duration > 0 && distance > 0 && strokeType.isNotEmpty() && effortLevel in 1..10 && heartRate > 0
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Validates the input fields and creates an updated training object.
     *
     * @return The updated [Trainings] object if the inputs are valid, or `null` if validation fails.
     */
    private fun validateAndUpdateTraining(): Trainings? {
        val date = etDate.text.toString()
        val duration = etDuration.text.toString().toIntOrNull() ?: -1
        val distance = etDistance.text.toString().toIntOrNull() ?: -1
        val strokeType = etStrokeType.text.toString()
        val effortLevel = etEffortLevel.text.toString().toIntOrNull() ?: -1
        val heartRate = etHeartRate.text.toString().toIntOrNull() ?: -1

        return if (validateInputs(date, duration, distance, strokeType, effortLevel, heartRate)) {
            val speed = HealthUtils.calculateSpeed(distance, duration)
            val calories = HealthUtils.calculateCalories(userWeight, duration, effortLevel)
            val heartRateZone = HealthUtils.classifyHeartRate(heartRate)

            training.copy(
                date = date,
                duration_minutes = duration,
                distance_meters = distance,
                stroke_type = strokeType,
                effort_level = effortLevel,
                heart_rate = heartRate,
                speed_mps = speed,
                calories_burned = calories,
                heart_rate_zone = heartRateZone
            )
        } else {
            Toast.makeText(this, "Invalid inputs", Toast.LENGTH_SHORT).show()
            null
        }
    }

    /**
     * Updates the training data in Firestore.
     *
     * @param updatedTraining The updated [Trainings] object containing the modified data.
     */
    private fun updateTrainingInFirestore(updatedTraining: Trainings) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null && training.id.isNotEmpty()) {
            val updatedData = hashMapOf(
                "date" to updatedTraining.date,
                "duration_minutes" to updatedTraining.duration_minutes,
                "distance_meters" to updatedTraining.distance_meters,
                "stroke_type" to updatedTraining.stroke_type,
                "effort_level" to updatedTraining.effort_level,
                "heart_rate" to updatedTraining.heart_rate,
                "speed_mps" to updatedTraining.speed_mps,
                "calories_burned" to updatedTraining.calories_burned,
                "heart_rate_zone" to updatedTraining.heart_rate_zone
            )

            FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .collection("trainings")
                .document(training.id)
                .update(updatedData as Map<String, Any>)
                .addOnSuccessListener {
                    setResult(RESULT_OK)
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Update failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}