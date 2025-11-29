package ju.mad.tuitioncounter.data.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import ju.mad.tuitioncounter.data.database.entity.ClassLogEntity
import ju.mad.tuitioncounter.data.database.entity.TuitionEntity

data class TuitionWithClasses(
    @Embedded val tuition: TuitionEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "tuitionId"
    )
    val classes: List<ClassLogEntity>
)
