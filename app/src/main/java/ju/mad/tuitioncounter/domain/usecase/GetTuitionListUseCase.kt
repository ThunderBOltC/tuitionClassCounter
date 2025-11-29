package ju.mad.tuitioncounter.domain.usecase

import jakarta.inject.Inject
import ju.mad.tuitioncounter.domain.model.TuitionModel
import ju.mad.tuitioncounter.domain.repository.TuitionRepository
import kotlinx.coroutines.flow.Flow

class GetTuitionListUseCase @Inject constructor(private val tuitionRepository: TuitionRepository) {

    // Get all tuitions with their class count
    suspend fun execute(): Flow<List<TuitionModel>> {
        return tuitionRepository.getAllTuitionsWithClassCount()
    }
}
