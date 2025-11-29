package ju.mad.tuitioncounter.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "class_logs",
    foreignKeys = [
        ForeignKey(
            entity = TuitionEntity::class,
            parentColumns = ["id"],
            childColumns = ["tuitionId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ClassLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val tuitionId: Long,
    val entryTimestampMs: Long
)
