package com.example.mecateknik.db
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mecateknik.db.dao.AutoPartDao
import com.example.mecateknik.db.dao.CarDao
import com.example.mecateknik.db.dao.MaintenanceBookDao
import com.example.mecateknik.db.dao.UserDao
import com.example.mecateknik.db.entities.AutoPartEntity
import com.example.mecateknik.db.entities.UserEntity
import com.example.mecateknik.db.entities.CarEntity
import com.example.mecateknik.db.entities.MaintenanceBookEntity

@Database(
    entities = [
        UserEntity::class,
        CarEntity::class,
        MaintenanceBookEntity::class,
        AutoPartEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun carDao(): CarDao
    abstract fun maintenanceBookDao(): MaintenanceBookDao
    abstract fun autoPartDao(): AutoPartDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mecateknik_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
