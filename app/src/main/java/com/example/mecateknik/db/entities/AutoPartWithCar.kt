package com.example.mecateknik.db.entities

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Relation entre une pièce auto et le véhicule auquel elle est associée.
 */
data class AutoPartWithCar(
    @Embedded val autoPart: AutoPartEntity,
    @Relation(
        parentColumn = "car_id",
        entityColumn = "id"
    )
    val car: CarEntity
)
