package com.example.lab6ex4

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultsActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        val tvSpeed = findViewById<TextView>(R.id.tvSpeed)
        val tvHeartRateZone = findViewById<TextView>(R.id.tvHeartRateZone)
        val tvCalories = findViewById<TextView>(R.id.tvCalories)
        val tvTips = findViewById<TextView>(R.id.tvTips)

        // Otrzymujemy dane przekazane z AddTrainingActivity
        val speed = intent.getDoubleExtra("speed", 0.0)
        val heartRateZone = intent.getStringExtra("heartRateZone") ?: ""
        val calories = intent.getDoubleExtra("calories", 0.0)
        tvCalories.text = "Calories Burned: $calories"

        val tips = intent.getStringExtra("tips") ?: ""

        // Ustawiamy dane na odpowiednich widokach
        tvSpeed.text = "Speed: $speed m/s"
        tvHeartRateZone.text = "Heart Rate Zone: $heartRateZone"
        tvCalories.text = "Calories Burned: $calories"
        tvTips.text = "Swimming Tips: $tips"
    }
}
