package com.example.lab6ex4

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

class AddTrainingActivity : AppCompatActivity() {

    private lateinit var etDate: EditText
    private lateinit var etDuration: EditText
    private lateinit var etDistance: EditText
    private lateinit var etStrokeType: EditText
    private lateinit var etEffortLevel: EditText
    private lateinit var etHeartRate: EditText
    private lateinit var btnSave: Button
    private var userWeight: Float = 0f

    private lateinit var databaseManager: DatabaseManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_training)

        // Initialize UI components
        etDate = findViewById(R.id.etDate)
        etDuration = findViewById(R.id.etDuration)
        etDistance = findViewById(R.id.etDistance)
        etStrokeType = findViewById(R.id.etStrokeType)
        etEffortLevel = findViewById(R.id.etEffortLevel)
        etHeartRate = findViewById(R.id.etHeartRate)
        btnSave = findViewById(R.id.btnSave)

        // Initialize DatabaseManager
        databaseManager = DatabaseManager()

        // Load user weight from Firestore
        loadUserWeight()

        // Set up Save button click listener
        btnSave.setOnClickListener {
            val date = etDate.text.toString()
            val duration = etDuration.text.toString().toIntOrNull() ?: -1
            val distance = etDistance.text.toString().toIntOrNull() ?: -1
            val strokeType = etStrokeType.text.toString()
            val effortLevel = etEffortLevel.text.toString().toIntOrNull() ?: -1
            val heartRate = etHeartRate.text.toString().toIntOrNull() ?: -1

            // Validate inputs
            if (!validateInputs(date, duration, distance, strokeType, effortLevel, heartRate)) {
                Toast.makeText(this, "Please fill in all fields correctly.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Calculate speed, calories, heart rate zone, and swimming tips
            val speed = HealthUtils.calculateSpeed(distance, duration)
            val heartRateZone = HealthUtils.classifyHeartRate(heartRate)
            val calories = HealthUtils.calculateCalories(userWeight, duration, effortLevel)
            val tips = HealthUtils.getSwimmingTips(heartRateZone)

            // Save training using DatabaseManager
            saveTrainingWithDatabaseManager(
                date, duration, distance, strokeType, effortLevel, heartRate, speed, calories, heartRateZone
            )

            // Launch ResultsActivity with calculated data
            val intent = Intent(this, ResultsActivity::class.java).apply {
                putExtra("speed", speed)
                putExtra("heartRateZone", heartRateZone)
                putExtra("calories", calories)
                putExtra("tips", tips)
            }
            startActivity(intent)
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
        // Walidacja daty (format dd/MM/yyyy)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dateFormat.isLenient = false // Nie akceptuj nieprawidłowych dat (np. 30 lutego)
        return try {
            dateFormat.parse(date) // Jeśli data jest nieprawidłowa, rzuci wyjątek
            duration > 0 && distance > 0 && strokeType.isNotEmpty() && effortLevel > 0 && heartRate > 0
        } catch (e: Exception) {
            false
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
                    if (document.exists()) {
                        // Pobieranie obiektu health jako mapy
                        val health = document.get("health") as? Map<*, *>

                        // Pobranie pola weight z mapy
                        userWeight = (health?.get("weight") as? Number)?.toFloat() ?: 0f

                        // Sprawdzenie, czy udało się pobrać wagę
                        if (userWeight > 0f) {
                            Toast.makeText(this, "User weight loaded: $userWeight kg", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Weight not set in Firestore. Configure your health data.", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this, "User data not found.", Toast.LENGTH_LONG).show()
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error when loading user weight", e)
                    Toast.makeText(this, "Error while loading data from Firestore.", Toast.LENGTH_LONG).show()
                }
        } else {
            Toast.makeText(this, "User not logged.", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Saves the training data to Firestore using the DatabaseManager.
     *
     * @param date The date of the training.
     * @param duration The duration of the training in minutes.
     * @param distance The distance covered in meters.
     * @param strokeType The type of swimming stroke.
     * @param effortLevel The perceived effort level (1-10).
     * @param heartRate The average heart rate during the training.
     * @param speed The calculated speed in meters per second.
     * @param calories The calculated calories burned.
     * @param heartRateZone The classified heart rate zone.
     */
    private fun saveTrainingWithDatabaseManager(
        date: String,
        duration: Int,
        distance: Int,
        strokeType: String,
        effortLevel: Int,
        heartRate: Int,
        speed: Double,
        calories: Double,
        heartRateZone: String
    ) {
        val trainingData = mapOf(
            "date" to date,
            "duration_minutes" to duration,
            "distance_meters" to distance,
            "stroke_type" to strokeType,
            "effort_level" to effortLevel,
            "heart_rate" to heartRate,
            "speed_mps" to speed,
            "calories_burned" to calories,
            "heart_rate_zone" to heartRateZone
        )

        databaseManager.saveTraining(trainingData) { success, error ->
            if (success) {
                Toast.makeText(this, "Training saved successfully.", Toast.LENGTH_SHORT).show()
            } else {
                Log.e("AddTrainingActivity", "Error saving training: ${error ?: "Unknown error"}")
                Toast.makeText(this, "Failed to save training: ${error ?: "Unknown error"}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}







