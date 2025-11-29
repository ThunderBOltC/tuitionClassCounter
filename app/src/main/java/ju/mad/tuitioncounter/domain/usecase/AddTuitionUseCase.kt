package ju.mad.tuitioncounter.domain.usecase

import jakarta.inject.Inject
import ju.mad.tuitioncounter.domain.model.TuitionModel
import ju.mad.tuitioncounter.domain.repository.TuitionRepository

class AddTuitionUseCase @Inject constructor(private val tuitionRepository: TuitionRepository) {

    // Adds a new tuition to the repository
    suspend fun execute(tuition: TuitionModel) {
        tuitionRepository.insertTuition(tuition)
    }
}
