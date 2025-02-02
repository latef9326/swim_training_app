package com.example.lab6ex4

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

/**
 * Main activity of the application.
 *
 * This activity serves as the main hub for the user after logging in. It provides navigation to
 * other features such as adding a training, viewing training history, managing health settings,
 * and logging out. It also displays a welcome message with the user's email.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        val btnAddTraining = findViewById<Button>(R.id.btnAddTraining)
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        val btnHealthSettings = findViewById<Button>(R.id.btnHealthSettings)
        val tvWelcome = findViewById<TextView>(R.id.tvWelcome)
        val btnViewTrainings = findViewById<Button>(R.id.btnViewTrainings)

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Check if the user is logged in
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Display welcome message with the user's email
            tvWelcome.text = "Welcome, ${currentUser.email}!"
        } else {
            // Redirect to LoginActivity if the user is not logged in
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Set click listener for the "Add Training" button
        btnAddTraining.setOnClickListener {
            // Navigate to AddTrainingActivity
            startActivity(Intent(this, AddTrainingActivity::class.java))
        }

        // Set click listener for the "Logout" button
        btnLogout.setOnClickListener {
            // Sign out the user and redirect to LoginActivity
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Set click listener for the "Health Settings" button
        btnHealthSettings.setOnClickListener {
            // Navigate to HealthSettingsActivity
            val intent = Intent(this, HealthSettingsActivity::class.java)
            startActivity(intent)
        }

        // Set click listener for the "View Trainings" button
        btnViewTrainings.setOnClickListener {
            // Navigate to TrainingListActivity
            val intent = Intent(this, TrainingListActivity::class.java)
            startActivity(intent)
        }
    }
}








