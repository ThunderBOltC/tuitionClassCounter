package ju.mad.tuitioncounter.data.mapper

import ju.mad.tuitioncounter.data.database.entity.TuitionEntity
import ju.mad.tuitioncounter.domain.model.TuitionModel

fun TuitionEntity.toDomain() = TuitionModel(
    id = id,
    name = name,
    location = location,
    salary = salary,
    targetedClass = targetedClass,
    startDateEpochMs = startDateEpochMs
)
