package com.example.lab6ex4

import kotlin.math.round

object HealthUtils {

    fun calculateSpeed(distanceMeters: Int, durationMinutes: Int): Double {
        return if (durationMinutes > 0) {
            val speed = distanceMeters.toDouble() / (durationMinutes * 60)
            roundToTwoDecimalPlaces(speed) // Zaokrąglamy do dwóch miejsc po przecinku
        } else {
            0.0
        }
    }

    fun classifyHeartRate(heartRate: Int): String {
        return when (heartRate) {
            in 100..120 -> "Recovery Zone (Zone 0)"
            in 120..140 -> "Aerobic Zone (Zone 1)"
            in 141..170 -> "Threshold Zone (Zone 2)"
            in 171..190 -> "Intense Zone (Zone 3)"
            else -> "Maximal Effort Zone (Zone 4)"
        }
    }

    fun calculateCalories(weight: Float, durationMinutes: Int, effortLevel: Int): Double {
        val caloriesPerMinute = weight * 0.0175 * effortLevel
        val totalCalories = caloriesPerMinute * durationMinutes
        return roundToTwoDecimalPlaces(totalCalories) // Zaokrąglamy do dwóch miejsc po przecinku
    }

    fun getSwimmingTips(heartRateZone: String): String {
        return when (heartRateZone) {
            "Recovery Zone (Zone 0)" -> {
                "Slow down and focus on recovery. Use light, controlled swimming strokes, such as freestyle or backstroke, to improve circulation and reduce muscle tension. Focus on deep breathing and a steady rhythm to support your body's recovery process."
            }
            "Aerobic Zone (Zone 1)" -> {
                "Maintain a steady pace and focus on improving your stamina. Opt for consistent strokes like freestyle and backstroke to enhance endurance. Keep your breathing steady and controlled to build aerobic capacity."
            }
            "Threshold Zone (Zone 2)" -> {
                "Push your limits to improve endurance. Increase your pace slightly while maintaining good technique. Use interval training and challenge yourself to hold a pace just below your lactate threshold to improve performance."
            }
            "Intense Zone (Zone 3)" -> {
                "Maximize effort for speed and power. Perform sprints with maximum intensity, focusing on technique and explosive movements. Use short bursts of effort followed by brief recovery periods to enhance speed and strength."
            }
            else -> {
                "Go all out and push for your maximum! Focus on maximum performance, incorporating fast-paced strokes and explosive movements to challenge your body and improve your overall swimming capabilities."
            }
        }


}

    private fun roundToTwoDecimalPlaces(value: Double): Double {
        return round(value * 100) / 100 // Zaokrąglenie do dwóch miejsc po przecinku
    }
}


