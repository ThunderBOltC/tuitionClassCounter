package ju.mad.tuitioncounter.data.mapper

import ju.mad.tuitioncounter.data.database.entity.ClassLogEntity
import ju.mad.tuitioncounter.domain.model.ClassLogModel

fun ClassLogEntity.toDomain() = ClassLogModel(
    id = id,
    tuitionId = tuitionId,
    entryTimestampMs = entryTimestampMs
)
