package ju.mad.tuitioncounter.domain.usecase

import ju.mad.tuitioncounter.domain.repository.TuitionRepository
import ju.mad.tuitioncounter.domain.model.TuitionModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LogClassUseCase(
    private val tuitionRepository: TuitionRepository
) {
    // Logs the class for a specific tuition ID
    suspend operator fun invoke(tuitionId: Long) {
        withContext(Dispatchers.IO) {
            // Insert a class log for the given tuition ID
            tuitionRepository.insertClassLog(tuitionId)
        }
    }
}
