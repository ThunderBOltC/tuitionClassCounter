package ju.mad.tuitioncounter.domain.model

data class ClassLogModel(
    val id: Long = 0L,
    val tuitionId: Long,
    val entryTimestampMs: Long // epoch millis when class was logged
)
