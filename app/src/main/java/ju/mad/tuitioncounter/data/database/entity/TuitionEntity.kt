package ju.mad.tuitioncounter.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "tuitions")
data class TuitionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val location: String,
    val salary: Double,
    val targetedClass: Int,
    val classCount: Int = 0,
    val startDateEpochMs: Long // Timestamp of the start date (Use Calendar or Date for conversion)
)
