package com.example.mecateknik.db.dao
import androidx.room.*
import com.example.mecateknik.db.entities.UserEntity
import com.example.mecateknik.db.entities.UserWithCars

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Query("SELECT * FROM users WHERE firebase_uid = :firebaseUid")
    suspend fun getUserByFirebaseId(firebaseUid: String): UserEntity?

    @Transaction
    @Query("SELECT * FROM users WHERE firebase_uid = :firebaseUid")
    suspend fun getUserWithCars(firebaseUid: String): UserWithCars
    @Delete
    suspend fun deleteUser(user: UserEntity)

}

