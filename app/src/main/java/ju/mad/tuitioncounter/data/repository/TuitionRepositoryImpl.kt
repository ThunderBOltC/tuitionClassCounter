package ju.mad.tuitioncounter.data.repository

import jakarta.inject.Inject
import ju.mad.tuitioncounter.data.database.TuitionDao
import ju.mad.tuitioncounter.data.database.entity.ClassLogEntity
import ju.mad.tuitioncounter.data.database.mappers.TuitionMapper
import ju.mad.tuitioncounter.data.database.mappers.toDomain
import ju.mad.tuitioncounter.domain.model.TuitionModel
import ju.mad.tuitioncounter.domain.repository.TuitionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TuitionRepositoryImpl @Inject constructor(private val tuitionDao: TuitionDao) : TuitionRepository {

    // Get all tuitions with class count
    override suspend fun getAllTuitionsWithClassCount(): Flow<List<TuitionModel>> {
        return tuitionDao.getAllTuitionsWithClassesFlow().map { tuitionsWithClasses ->
            tuitionsWithClasses.map { tuitionWithClasses ->
                // Map each tuition with class logs to tuition model
                tuitionWithClasses.tuition.toDomain().apply {
                    // You can calculate class count here and add it to the tuition model, if needed
                    val classCount = tuitionWithClasses.classes.size
                    this.copy(targetedClass = classCount) // example: setting class count
                }
            }
        }
    }

    // Get details of a single tuition (by id)
    override suspend fun getTuitionDetails(id: Long): Flow<TuitionModel> {
        return tuitionDao.getTuitionWithClassesFlow(id).map { tuitionWithClasses ->
            tuitionWithClasses?.tuition?.toDomain() ?: throw IllegalArgumentException("Tuition not found")
        }
    }
    override suspend fun insertTuition(tuition: TuitionModel) {
        val tuitionEntity = TuitionMapper.toEntity(tuition) // Convert model to entity
        tuitionDao.insertTuition(tuitionEntity) // Insert into the database
    }
    // Insert a class log for a specific tuition
    override suspend fun insertClassLog(tuitionId: Long) {
        val classLog = ClassLogEntity(
            tuitionId = tuitionId,
            entryTimestampMs = System.currentTimeMillis() // Current time in epoch millis
        )
        tuitionDao.insertClassLog(classLog)
    }

    // Delete all class logs for a specific tuition (reset)
    override suspend fun deleteAllClassLogs(tuitionId: Long) {
        tuitionDao.clearClassLogsForTuition(tuitionId)
    }
    override suspend fun updateTuition(tuition: TuitionModel) {
        val tuitionEntity = TuitionMapper.toEntity(tuition)
        tuitionDao.updateTuition(tuitionEntity)
    }

    override suspend fun deleteTuition(tuition: TuitionModel) {
        val tuitionEntity = TuitionMapper.toEntity(tuition)
        tuitionDao.deleteTuition(tuitionEntity)
    }
    override suspend fun resetClassCount(tuitionId: Long) {
        deleteAllClassLogs(tuitionId)
    }



}
