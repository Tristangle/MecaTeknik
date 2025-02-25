package com.example.mecateknik.db.dao;
import androidx.room.*
import com.example.mecateknik.db.entities.CarEntity

@Dao
interface CarDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCar(car: CarEntity): Long

    @Query("SELECT * FROM cars WHERE user_uid = :userUid")
    suspend fun getCarsByUser(userUid: String): List<CarEntity>

    @Query("SELECT * FROM cars WHERE user_uid = :userUid AND country_origin = :country")
    suspend fun getCarsByCountry(userUid: String, country: String): List<CarEntity>

    @Query("SELECT * FROM cars WHERE user_uid = :userUid AND engine_type = :engine")
    suspend fun getCarsByEngine(userUid: String, engine: String): List<CarEntity>

    @Query("SELECT * FROM cars WHERE user_uid = :userUid AND specification = :spec")
    suspend fun getCarsBySpec(userUid: String, spec: String): List<CarEntity>

    @Delete
    suspend fun deleteCar(car: CarEntity)
}
