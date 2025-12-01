package ju.mad.tuitioncounter.data.database

import androidx.room.*
import ju.mad.tuitioncounter.data.database.entity.ClassLogEntity
import ju.mad.tuitioncounter.data.database.entity.TuitionEntity
import ju.mad.tuitioncounter.data.database.relation.TuitionWithClasses
import kotlinx.coroutines.flow.Flow

@Dao
interface TuitionDao {

    // TUITION operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTuition(entity: TuitionEntity): Long

    @Update
    suspend fun updateTuition(entity: TuitionEntity)

    @Delete
    suspend fun deleteTuition(entity: TuitionEntity)

    // Query to reset class count to 0 (resets tuition's class count)
    @Query("UPDATE tuitions SET classCount = 0 WHERE id = :tuitionId")
    suspend fun resetClassCount(tuitionId: Long)

    // Return live list of tuitions (without classes)
    @Query("SELECT * FROM tuitions ORDER BY startDateEpochMs DESC")
    fun getAllTuitionsFlow(): Flow<List<TuitionEntity>>

    // Relationship query returns tuition with its classes
    @Transaction
    @Query("SELECT * FROM tuitions ORDER BY startDateEpochMs DESC")
    fun getAllTuitionsWithClassesFlow(): Flow<List<TuitionWithClasses>>

    @Transaction
    @Query("SELECT * FROM tuitions WHERE id = :tuitionId LIMIT 1")
    fun getTuitionWithClassesFlow(tuitionId: Long): Flow<TuitionWithClasses?>

    // CLASS LOG operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClassLog(log: ClassLogEntity): Long

    @Query("SELECT * FROM class_logs WHERE tuitionId = :tuitionId ORDER BY entryTimestampMs DESC")
    fun getClassLogsForTuitionFlow(tuitionId: Long): Flow<List<ClassLogEntity>>

    // Clear all class logs for a specific tuition
    @Query("DELETE FROM class_logs WHERE tuitionId = :tuitionId")
    suspend fun clearClassLogsForTuition(tuitionId: Long)

    @Delete
    suspend fun deleteClassLog(log: ClassLogEntity)
    @Query("DELETE FROM class_logs WHERE tuitionId = :tuitionId")
    suspend fun deleteAllClassLogsForTuition(tuitionId: Long)
// Add this method to your TuitionDao.kt interface

    @Query("DELETE FROM class_logs WHERE id = :classLogId")
    suspend fun deleteClassLogById(classLogId: Long)
}
