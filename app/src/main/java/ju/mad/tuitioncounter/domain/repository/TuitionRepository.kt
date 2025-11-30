package ju.mad.tuitioncounter.domain.repository

import ju.mad.tuitioncounter.domain.model.ClassLogModel
import ju.mad.tuitioncounter.domain.model.TuitionModel
import kotlinx.coroutines.flow.Flow

interface TuitionRepository {

    // Get all tuitions with class count
    suspend fun getAllTuitionsWithClassCount(): Flow<List<TuitionModel>>

    // Get details for a specific tuition by id
    suspend fun getTuitionDetails(id: Long): Flow<TuitionModel>

    // Get class logs for a specific tuition
    fun getClassLogsForTuition(tuitionId: Long): Flow<List<ClassLogModel>>

    // Insert a new tuition
    suspend fun insertTuition(tuition: TuitionModel)

    // Update existing tuition
    suspend fun updateTuition(tuition: TuitionModel)

    // Delete tuition
    suspend fun deleteTuition(tuition: TuitionModel)

    // Log a class (insert class log)
    suspend fun insertClassLog(tuitionId: Long)
}