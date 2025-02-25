package com.example.mecateknik.db.entities
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.ForeignKey
import androidx.room.Index
import java.util.*

@Entity(
    tableName = "cars",
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["firebase_uid"],
        childColumns = ["user_uid"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["user_uid"])]
)
data class CarEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "user_uid")
    val userUid: String,

    @ColumnInfo(name = "brand")
    val brand: String,

    @ColumnInfo(name = "model")
    val model: String,

    @ColumnInfo(name = "year")
    val year: Int,

    @ColumnInfo(name = "engine_type")
    val engineType: String,

    @ColumnInfo(name = "specification")
    val specification: String,

    @ColumnInfo(name = "country_origin")
    val countryOrigin: String
)
