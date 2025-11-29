package ju.mad.tuitioncounter.domain.model

data class TuitionModel(
    val id: Long = 0L,
    val name: String,
    val location: String,
    val salary: Double,
    val targetedClass: Int,
    val classCount: Int = 0,  // Number of classes conducted
    val startDateEpochMs: Long // epoch millis for the selected start date
) {
    // Calculate progress
    val progress: String
        get() = "$classCount / $targetedClass"
}