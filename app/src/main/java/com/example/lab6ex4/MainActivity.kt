package com.example.lab6ex4

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnAddTraining = findViewById<Button>(R.id.btnAddTraining)
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        val btnHealthSettings = findViewById<Button>(R.id.btnHealthSettings)
        val tvWelcome = findViewById<TextView>(R.id.tvWelcome)

        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            tvWelcome.text = "Welcome, ${currentUser.email}!"
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        btnAddTraining.setOnClickListener {
            // Przejście do aktywności dodawania treningu
            startActivity(Intent(this, AddTrainingActivity::class.java))
        }

        btnLogout.setOnClickListener {
            // Wylogowanie i przejście do ekranu logowania
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        btnHealthSettings.setOnClickListener {
            // Przejście do ustawień zdrowia
            val intent = Intent(this, HealthSettingsActivity::class.java)
            startActivity(intent)
        }

        // Dodajmy nowy przycisk do przejścia do TrainingListActivity
        val btnViewTrainings = findViewById<Button>(R.id.btnViewTrainings)
        btnViewTrainings.setOnClickListener {
            // Przejście do aktywności z listą treningów
            val intent = Intent(this, TrainingListActivity::class.java)
            startActivity(intent)
        }
    }
}








