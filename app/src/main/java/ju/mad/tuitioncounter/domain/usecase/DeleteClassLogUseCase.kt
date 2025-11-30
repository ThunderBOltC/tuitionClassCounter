package ju.mad.tuitioncounter.domain.usecase

import ju.mad.tuitioncounter.domain.repository.TuitionRepository

class DeleteClassLogUseCase(private val repository: TuitionRepository) {
    suspend fun execute(classId: Long) {
        repository.deleteClassLog(classId)
    }
}