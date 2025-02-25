package com.example.mecateknik.db.entities

import androidx.room.Embedded
import androidx.room.Relation


data class UserWithCars(
    @Embedded val user: UserEntity,
    @Relation(
        parentColumn = "firebase_uid",
        entityColumn = "user_uid"
    )
    val cars: List<CarEntity>
)
