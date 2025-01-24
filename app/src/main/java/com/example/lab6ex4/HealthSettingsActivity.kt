package com.example.lab6ex4

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class HealthSettingsActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health_settings)

        auth = FirebaseAuth.getInstance()

        val etHeight = findViewById<EditText>(R.id.etHeight)
        val etWeight = findViewById<EditText>(R.id.etWeight)
        val etRestingHeartRate = findViewById<EditText>(R.id.etRestingHeartRate)
        val etSwimmingLevel = findViewById<EditText>(R.id.etSwimmingLevel)
        val btnSave = findViewById<Button>(R.id.btnSaveHealthData)

        btnSave.setOnClickListener {
            val height = etHeight.text.toString().toFloatOrNull() ?: 0f
            val weight = etWeight.text.toString().toFloatOrNull() ?: 0f
            val restingHeartRate = etRestingHeartRate.text.toString().toIntOrNull() ?: 0
            val swimmingLevel = etSwimmingLevel.text.toString()

            // Walidacja danych wejściowych
            if (height <= 0 || weight <= 0 || restingHeartRate <= 0 || swimmingLevel.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields correctly.", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val userId = auth.currentUser?.uid
            if (userId != null) {
                val healthData = hashMapOf(
                    "health" to hashMapOf(  // Dodajemy dane zdrowotne do sekcji "health"
                        "height" to height,
                        "weight" to weight,
                        "restingHeartRate" to restingHeartRate,
                        "swimmingLevel" to swimmingLevel
                    )
                )

                val db = FirebaseFirestore.getInstance()
                db.collection("users").document(userId)
                    .set(healthData, SetOptions.merge()) // Zapisywanie w sekcji "health"
                    .addOnSuccessListener {
                        Toast.makeText(this, "Health data saved successfully!", Toast.LENGTH_SHORT).show()
                        finish()
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


