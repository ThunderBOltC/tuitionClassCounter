package ju.mad.tuitioncounter.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ju.mad.tuitioncounter.data.database.entity.ClassLogEntity
import ju.mad.tuitioncounter.data.database.entity.TuitionEntity

@Database(
    entities = [TuitionEntity::class, ClassLogEntity::class],
    version = 2,
    exportSchema = false
)
abstract class TuitionDatabase : RoomDatabase() {

    // DAO for tuition operations
    abstract fun tuitionDao(): TuitionDao

    companion object {
        // Singleton instance to prevent multiple instances of database opening
        @Volatile
        private var INSTANCE: TuitionDatabase? = null

        fun getDatabase(context: Context): TuitionDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TuitionDatabase::class.java,
                    "tuition_database"
                )
                    .fallbackToDestructiveMigration()   // <-- wipes and rebuilds DB on schema change
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
