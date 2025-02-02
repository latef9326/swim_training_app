package com.example.lab6ex4

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * Activity that displays the results of a swimming training session,
 * including speed, heart rate zone, calories burned, and swimming tips.
 */
class ResultsActivity : AppCompatActivity() {

    /**
     * Called when the activity is starting. Sets up the UI and retrieves
     * the data passed from AddTrainingActivity.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down, this Bundle contains the most recent data.
     */
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        // Initialize UI elements
        val tvSpeed = findViewById<TextView>(R.id.tvSpeed)
        val tvHeartRateZone = findViewById<TextView>(R.id.tvHeartRateZone)
        val tvCalories = findViewById<TextView>(R.id.tvCalories)
        val tvTips = findViewById<TextView>(R.id.tvTips)

        // Retrieve data from the intent
        val speed = intent.getDoubleExtra("speed", 0.0)
        val heartRateZone = intent.getStringExtra("heartRateZone") ?: ""
        val calories = intent.getDoubleExtra("calories", 0.0)
        val tips = intent.getStringExtra("tips") ?: ""

        // Display the retrieved data in the UI
        tvSpeed.text = "Speed: $speed m/s"
        tvHeartRateZone.text = "Heart Rate Zone: $heartRateZone"
        tvCalories.text = "Calories Burned: $calories"
        tvTips.text = "Swimming Tips: $tips"
    }
}

