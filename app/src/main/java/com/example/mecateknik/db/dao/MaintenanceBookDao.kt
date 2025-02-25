package com.example.mecateknik.db.dao

import androidx.room.*
import com.example.mecateknik.db.entities.MaintenanceBookEntity

@Dao
interface MaintenanceBookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMaintenance(maintenance: MaintenanceBookEntity): Long

    @Query("SELECT * FROM maintenance_books WHERE car_id = :carId ORDER BY date DESC")
    suspend fun getMaintenanceForCar(carId: String): List<MaintenanceBookEntity>

    @Delete
    suspend fun deleteMaintenance(maintenance: MaintenanceBookEntity)
}
