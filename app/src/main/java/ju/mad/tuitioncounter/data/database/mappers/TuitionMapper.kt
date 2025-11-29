package ju.mad.tuitioncounter.data.database.mappers

import ju.mad.tuitioncounter.domain.model.TuitionModel
import ju.mad.tuitioncounter.data.database.entity.TuitionEntity

object TuitionMapper {

    // Convert Entity to Model
    fun toModel(entity: TuitionEntity): TuitionModel {
        return TuitionModel(
            id = entity.id,
            name = entity.name,
            location = entity.location,
            salary = entity.salary,
            targetedClass = entity.targetedClass,
            startDateEpochMs = entity.startDateEpochMs
        )
    }

    // Convert Model to Entity
    fun toEntity(model: TuitionModel): TuitionEntity {
        return TuitionEntity(
            id = model.id,
            name = model.name,
            location = model.location,
            salary = model.salary,
            targetedClass = model.targetedClass,
            startDateEpochMs = model.startDateEpochMs
        )
    }
}
