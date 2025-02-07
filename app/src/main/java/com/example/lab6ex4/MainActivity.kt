package com.example.lab6ex4

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicjalizacja widoków
        val btnAddTraining = findViewById<Button>(R.id.btnAddTraining)
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        val btnHealthSettings = findViewById<Button>(R.id.btnHealthSettings)
        val tvWelcome = findViewById<TextView>(R.id.tvWelcome)
        val btnViewTrainings = findViewById<Button>(R.id.btnViewTrainings)

        // Inicjalizacja Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Sprawdź czy użytkownik jest zalogowany
        val currentUser = auth.currentUser
        if (currentUser != null) {
            tvWelcome.text = "Welcome, ${currentUser.email}!"
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Sprawdź uprawnienia do powiadomień (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    0
                )
            }
        }

        // Nasłuchiwacze kliknięć
        btnAddTraining.setOnClickListener {
            startActivity(Intent(this, AddTrainingActivity::class.java))
        }

        btnLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        btnHealthSettings.setOnClickListener {
            startActivity(Intent(this, HealthSettingsActivity::class.java))
        }

        btnViewTrainings.setOnClickListener {
            startActivity(Intent(this, TrainingListActivity::class.java))
        }
    }
}