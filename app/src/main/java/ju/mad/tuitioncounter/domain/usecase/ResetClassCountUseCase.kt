package ju.mad.tuitioncounter.domain.usecase

import ju.mad.tuitioncounter.domain.repository.TuitionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ResetClassCountUseCase(
    private val tuitionRepository: TuitionRepository
) {
    // Resets the class count for the given tuition ID
    suspend operator fun invoke(tuitionId: Long) {
        withContext(Dispatchers.IO) {
            // Delete all class logs for the specified tuition ID
            tuitionRepository.deleteAllClassLogs(tuitionId)
        }
    }
}
