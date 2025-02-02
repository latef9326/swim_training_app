package com.example.lab6ex4

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

/**
 * Activity for managing and saving user health settings.
 *
 * This activity allows users to input and save their health-related data, such as height, weight,
 * resting heart rate, and swimming level. The data is stored in Firestore under the current user's
 * document in the "health" section.
 */
class HealthSettingsActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    /**
     * Called when the activity is starting. This is where most initialization should go.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                           shut down, this Bundle contains the data it most recently supplied
     *                           in `onSaveInstanceState`. Otherwise, it is null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health_settings)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize UI components
        val etHeight = findViewById<EditText>(R.id.etHeight)
        val etWeight = findViewById<EditText>(R.id.etWeight)
        val etRestingHeartRate = findViewById<EditText>(R.id.etRestingHeartRate)
        val etSwimmingLevel = findViewById<EditText>(R.id.etSwimmingLevel)
        val btnSave = findViewById<Button>(R.id.btnSaveHealthData)

        // Set up Save button click listener
        btnSave.setOnClickListener {
            // Parse input values
            val height = etHeight.text.toString().toFloatOrNull() ?: 0f
            val weight = etWeight.text.toString().toFloatOrNull() ?: 0f
            val restingHeartRate = etRestingHeartRate.text.toString().toIntOrNull() ?: 0
            val swimmingLevel = etSwimmingLevel.text.toString()

            // Validate inputs
            if (height <= 0 || weight <= 0 || restingHeartRate <= 0 || swimmingLevel.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields correctly.", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            // Get current user ID
            val userId = auth.currentUser?.uid
            if (userId != null) {
                // Prepare health data to be saved
                val healthData = hashMapOf(
                    "health" to hashMapOf(  // Store health data under the "health" section
                        "height" to height,
                        "weight" to weight,
                        "restingHeartRate" to restingHeartRate,
                        "swimmingLevel" to swimmingLevel
                    )
                )

                // Save data to Firestore
                val db = FirebaseFirestore.getInstance()
                db.collection("users").document(userId)
                    .set(healthData, SetOptions.merge()) // Merge with existing data
                    .addOnSuccessListener {
                        Toast.makeText(this, "Health data saved successfully!", Toast.LENGTH_SHORT).show()
                        finish() // Close the activity after saving
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error saving data: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


