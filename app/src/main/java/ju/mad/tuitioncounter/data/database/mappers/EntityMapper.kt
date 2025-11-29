package ju.mad.tuitioncounter.data.database.mappers

import ju.mad.tuitioncounter.data.database.entity.ClassLogEntity
import ju.mad.tuitioncounter.data.database.entity.TuitionEntity
import ju.mad.tuitioncounter.domain.model.ClassLogModel
import ju.mad.tuitioncounter.domain.model.TuitionModel

fun TuitionEntity.toDomain() = TuitionModel(
    id = id,
    name = name,
    location = location,
    salary = salary,
    targetedClass = targetedClass,
    startDateEpochMs = startDateEpochMs
)

fun TuitionModel.toEntity() = TuitionEntity(
    id = id,
    name = name,
    location = location,
    salary = salary,
    targetedClass = targetedClass,
    startDateEpochMs = startDateEpochMs
)

fun ClassLogEntity.toDomain() = ClassLogModel(
    id = id,
    tuitionId = tuitionId,
    entryTimestampMs = entryTimestampMs
)

fun ClassLogModel.toEntity() = ClassLogEntity(
    id = id,
    tuitionId = tuitionId,
    entryTimestampMs = entryTimestampMs
)
