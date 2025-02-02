import java.io.Serializable

/**
 * Data class representing a swimming training session.
 *
 * @property id Unique identifier for the training session.
 * @property date The date of the training session.
 * @property duration_minutes Duration of the training session in minutes.
 * @property distance_meters Distance swum during the session in meters.
 * @property stroke_type The type of swimming stroke used (e.g., freestyle, butterfly).
 * @property effort_level Subjective effort level of the session on a scale (e.g., 1-10).
 * @property heart_rate Average heart rate during the session in beats per minute (BPM).
 * @property speed_mps Average swimming speed in meters per second.
 * @property calories_burned Estimated number of calories burned during the session.
 * @property heart_rate_zone The heart rate zone classification during the session.
 */
data class Trainings(
    val id: String = "",
    val date: String = "",
    val duration_minutes: Int = 0,
    val distance_meters: Int = 0,
    val stroke_type: String = "",
    val effort_level: Int = 0,
    val heart_rate: Int = 0,
    val speed_mps: Double = 0.0,
    val calories_burned: Double = 0.0,
    val heart_rate_zone: String = ""
) : Serializable














