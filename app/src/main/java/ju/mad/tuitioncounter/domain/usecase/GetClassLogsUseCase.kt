package ju.mad.tuitioncounter.domain.usecase

import ju.mad.tuitioncounter.domain.model.ClassLogModel
import ju.mad.tuitioncounter.domain.repository.TuitionRepository
import kotlinx.coroutines.flow.Flow

class GetClassLogsUseCase(private val repository: TuitionRepository) {
    fun execute(tuitionId: Long): Flow<List<ClassLogModel>> {
        return repository.getClassLogsForTuition(tuitionId)
    }
}