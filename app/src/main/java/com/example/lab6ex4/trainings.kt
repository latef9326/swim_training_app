data class Trainings(
    val user_id: String = "",
    val date: String = "",
    val duration_minutes: Int = 0,
    val distance_meters: Int = 0,
    val stroke_type: String = "",
    val effort_level: Int = 0,
    val calories: Double = 0.0 // Dodajemy pole do przechowywania spalonych kalorii
) {




    // Metoda obliczająca prędkość na podstawie dystansu i czasu
    fun calculateSpeed(): Double {
        return if (duration_minutes > 0) {
            distance_meters.toDouble() / (duration_minutes * 60)
        } else {
            0.0
        }
    }

    // Metoda do określania strefy tętna na podstawie wartości tętna
    fun getHeartRateZone(heartRate: Int): String {
        return when (heartRate) {
            in 100..120 -> "Recovery Zone (Zone 0)"
            in 121..139 -> "Aerobic Zone (Zone 1)"
            in 140..160 -> "Threshold Zone (Zone 2)"
            in 170..180 -> "Intense Zone (Zone 3)"
            else -> "Maximal Effort Zone (Zone 4)"
        }
    }
}






