package com.example.mecateknik.db.entities

import androidx.room.*
import java.util.*

@Entity(
    tableName = "maintenance_books",
    foreignKeys = [ForeignKey(
        entity = CarEntity::class,
        parentColumns = ["id"],
        childColumns = ["car_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["car_id"])]
)
data class MaintenanceBookEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "car_id")
    val carId: String,

    @ColumnInfo(name = "date")
    val date: Long,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "kilometers")
    val kilometers: Int,

    @ColumnInfo(name = "garage")
    val garage: String?
)
