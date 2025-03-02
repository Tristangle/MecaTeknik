package com.example.mecateknik.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entité représentant un article du panier.
 */
@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userUid: String,
    val autoPartReference: String,
    val quantity: Int
)
