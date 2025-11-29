package ju.mad.tuitioncounter.domain.repository

import ju.mad.tuitioncounter.domain.model.TuitionModel
import kotlinx.coroutines.flow.Flow

interface TuitionRepository {

    // Get all tuitions with class count
    suspend fun getAllTuitionsWithClassCount(): Flow<List<TuitionModel>>

    // Get details for a specific tuition by id
    suspend fun getTuitionDetails(id: Long): Flow<TuitionModel>

    // Insert a new tuition
    suspend fun insertTuition(tuition: TuitionModel)

    suspend fun insertClassLog(tuitionId: Long)
    suspend fun deleteAllClassLogs(tuitionId: Long)
    // Reset the class count of a specific tuition (reset to 0)
    suspend fun resetClassCount(tuitionId: Long)

    // Delete a specific tuition
    suspend fun deleteTuition(tuition: TuitionModel)
    // Update an existing tuition
    suspend fun updateTuition(tuition: TuitionModel)
}
