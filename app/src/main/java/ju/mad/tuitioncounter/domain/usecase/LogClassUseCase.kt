package ju.mad.tuitioncounter.domain.usecase

import ju.mad.tuitioncounter.domain.repository.TuitionRepository

class LogClassUseCase(private val repository: TuitionRepository) {
    suspend fun execute(tuitionId: Long) {
        repository.insertClassLog(tuitionId)
    }
}