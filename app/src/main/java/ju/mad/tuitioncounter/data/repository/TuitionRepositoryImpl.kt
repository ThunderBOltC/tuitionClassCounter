package ju.mad.tuitioncounter.data.repository

import ju.mad.tuitioncounter.data.database.TuitionDao
import ju.mad.tuitioncounter.data.database.entity.ClassLogEntity
import ju.mad.tuitioncounter.data.database.mappers.TuitionMapper
import ju.mad.tuitioncounter.data.database.mappers.toDomain
import ju.mad.tuitioncounter.domain.model.ClassLogModel
import ju.mad.tuitioncounter.domain.model.TuitionModel
import ju.mad.tuitioncounter.domain.repository.TuitionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class TuitionRepositoryImpl(private val tuitionDao: TuitionDao) : TuitionRepository {

    // Get all tuitions with class count
    override suspend fun getAllTuitionsWithClassCount(): Flow<List<TuitionModel>> {
        return tuitionDao.getAllTuitionsWithClassesFlow().map { tuitionsWithClasses ->
            tuitionsWithClasses.map { tuitionWithClasses ->
                TuitionMapper.toModel(tuitionWithClasses.tuition).copy(
                    classCount = tuitionWithClasses.classes.size
                )
            }
        }
    }

    // Get details of a single tuition (by id)
    override suspend fun getTuitionDetails(id: Long): Flow<TuitionModel> {
        return tuitionDao.getTuitionWithClassesFlow(id).map { tuitionWithClasses ->
            tuitionWithClasses?.let {
                TuitionMapper.toModel(it.tuition).copy(
                    classCount = it.classes.size
                )
            } ?: throw IllegalArgumentException("Tuition not found")
        }
    }

    // Get class logs for a specific tuition
    override fun getClassLogsForTuition(tuitionId: Long): Flow<List<ClassLogModel>> {
        return tuitionDao.getClassLogsForTuitionFlow(tuitionId).map { classLogEntities ->
            classLogEntities.map { it.toDomain() }
        }
    }

    // Insert a new tuition
    override suspend fun insertTuition(tuition: TuitionModel) {
        val tuitionEntity = TuitionMapper.toEntity(tuition)
        tuitionDao.insertTuition(tuitionEntity)
    }

    // Update existing tuition
    override suspend fun updateTuition(tuition: TuitionModel) {
        val tuitionEntity = TuitionMapper.toEntity(tuition)
        tuitionDao.updateTuition(tuitionEntity)
    }

    // Delete tuition
    override suspend fun deleteTuition(tuition: TuitionModel) {
        val tuitionEntity = TuitionMapper.toEntity(tuition)
        tuitionDao.deleteTuition(tuitionEntity)
    }

    // Insert a class log with customized data time
    override suspend fun insertClassLog(tuitionId: Long, timestamp: Long) {
        val classLog = ClassLogEntity(
            tuitionId = tuitionId,
            entryTimestampMs = timestamp
        )
        tuitionDao.insertClassLog(classLog)
    }

    override suspend fun deleteClassLog(classId: Long) {
        val classLog = tuitionDao.getClassLogsForTuitionFlow(classId).first().find { it.id == classId }
        classLog?.let { tuitionDao.deleteClassLog(it) }
    }

    // Reset the class count for a specific tuition (salary reset)
    override suspend fun resetClassCount(tuitionId: Long) {
        tuitionDao.deleteAllClassLogsForTuition(tuitionId) // delete all class logs for the tuition
    }
}