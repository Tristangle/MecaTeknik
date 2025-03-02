package com.example.mecateknik.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.TypeConverters
import com.example.mecateknik.db.converters.CarModelConverter

/**
 * Entité représentant une pièce auto disponible en boutique.
 * Chaque pièce a une référence unique, un nom, une quantité en stock, un prix,
 * et est associée à une ou plusieurs configurations de voiture sous forme de chaîne "brand;model;year".
 */
@Entity(tableName = "auto_parts")
@TypeConverters(CarModelConverter::class)
data class AutoPartEntity(
    @PrimaryKey
    @ColumnInfo(name = "reference")
    val reference: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "quantity_in_stock")
    val quantityInStock: Int,

    @ColumnInfo(name = "price")
    val price: Double,

    // La liste des modèles de voitures associés, chacun sous la forme "brand;model;year"
    @ColumnInfo(name = "associated_models")
    val associatedModels: List<String>
)
