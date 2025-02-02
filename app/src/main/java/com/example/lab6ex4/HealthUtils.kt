package com.example.lab6ex4

import kotlin.math.round

/**
 * Utility object for health-related calculations and classifications.
 *
 * This object provides methods for calculating speed, classifying heart rate zones,
 * estimating calories burned, and generating swimming tips based on heart rate zones.
 */
object HealthUtils {

    /**
     * Calculates the speed in meters per second (m/s) based on distance and duration.
     *
     * @param distanceMeters The distance covered in meters.
     * @param durationMinutes The duration of the activity in minutes.
     * @return The calculated speed in m/s, rounded to two decimal places. Returns 0.0 if duration is invalid.
     */
    fun calculateSpeed(distanceMeters: Int, durationMinutes: Int): Double {
        return if (durationMinutes > 0) {
            val speed = distanceMeters.toDouble() / (durationMinutes * 60)
            roundToTwoDecimalPlaces(speed) // Round to two decimal places
        } else {
            0.0
        }
    }

    /**
     * Classifies the heart rate into zones based on the provided heart rate value.
     *
     * @param heartRate The heart rate value to classify.
     * @return A string representing the heart rate zone:
     *         - "Recovery Zone (Zone 0)" for 100-120 bpm
     *         - "Aerobic Zone (Zone 1)" for 120-140 bpm
     *         - "Threshold Zone (Zone 2)" for 141-170 bpm
     *         - "Intense Zone (Zone 3)" for 171-190 bpm
     *         - "Maximal Effort Zone (Zone 4)" for values outside the above ranges.
     */
    fun classifyHeartRate(heartRate: Int): String {
        return when (heartRate) {
            in 100..120 -> "Recovery Zone (Zone 0)"
            in 120..140 -> "Aerobic Zone (Zone 1)"
            in 141..170 -> "Threshold Zone (Zone 2)"
            in 171..190 -> "Intense Zone (Zone 3)"
            else -> "Maximal Effort Zone (Zone 4)"
        }
    }

    /**
     * Calculates the estimated calories burned during an activity.
     *
     * @param weight The weight of the user in kilograms.
     * @param durationMinutes The duration of the activity in minutes.
     * @param effortLevel The perceived effort level (1-10 scale).
     * @return The estimated calories burned, rounded to two decimal places.
     */
    fun calculateCalories(weight: Float, durationMinutes: Int, effortLevel: Int): Double {
        val caloriesPerMinute = weight * 0.0175 * effortLevel
        val totalCalories = caloriesPerMinute * durationMinutes
        return roundToTwoDecimalPlaces(totalCalories) // Round to two decimal places
    }

    /**
     * Provides swimming tips based on the user's heart rate zone.
     *
     * @param heartRateZone The heart rate zone as classified by [classifyHeartRate].
     * @return A string containing personalized swimming tips for the given heart rate zone.
     */
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

    /**
     * Rounds a double value to two decimal places.
     *
     * @param value The value to round.
     * @return The rounded value with two decimal places.
     */
    private fun roundToTwoDecimalPlaces(value: Double): Double {
        return round(value * 100) / 100 // Round to two decimal places
    }
}


