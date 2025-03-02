package com.example.mecateknik.db.dao

import androidx.room.*
import com.example.mecateknik.db.entities.CartItemEntity

@Dao
interface CartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: CartItemEntity): Long

    @Query("SELECT * FROM cart_items WHERE userUid = :userUid")
    suspend fun getCartItemsByUser(userUid: String): List<CartItemEntity>

    @Update
    suspend fun updateCartItem(cartItem: CartItemEntity)

    @Delete
    suspend fun deleteCartItem(cartItem: CartItemEntity)

    @Query("DELETE FROM cart_items WHERE userUid = :userUid")
    suspend fun clearCartForUser(userUid: String)
}
