package ju.mad.tuitioncounter.domain.usecase

import ju.mad.tuitioncounter.domain.model.TuitionModel
import ju.mad.tuitioncounter.domain.repository.TuitionRepository

class DeleteTuitionUseCase(private val repository: TuitionRepository) {
    suspend fun execute(tuition: TuitionModel) {
        repository.deleteTuition(tuition)
    }
}