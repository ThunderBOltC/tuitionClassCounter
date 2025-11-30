package ju.mad.tuitioncounter.domain.usecase

import ju.mad.tuitioncounter.domain.model.TuitionModel
import ju.mad.tuitioncounter.domain.repository.TuitionRepository
import kotlinx.coroutines.flow.Flow

class GetTuitionDetailsUseCase(private val repository: TuitionRepository) {
    suspend fun execute(tuitionId: Long): Flow<TuitionModel> {
        return repository.getTuitionDetails(tuitionId)
    }
}