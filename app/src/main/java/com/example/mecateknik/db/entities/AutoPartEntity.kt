package com.example.mecateknik.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.ForeignKey
import androidx.room.Index

/**
 * Entité représentant une pièce automobile.
 */
@Entity(
    tableName = "auto_parts",
    foreignKeys = [
        ForeignKey(
            entity = CarEntity::class,
            parentColumns = ["id"],
            childColumns = ["car_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["car_id"])]
)
data class AutoPartEntity(
    @PrimaryKey
    @ColumnInfo(name = "reference")
    val reference: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "car_id")
    val carId: String
)
