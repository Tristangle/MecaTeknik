package com.example.mecateknik.ui.cart

import com.example.mecateknik.db.entities.CartItemEntity
import com.example.mecateknik.db.entities.AutoPartEntity

data class CartItemDetail(
    val cartItem: CartItemEntity,
    val autoPart: AutoPartEntity?
)
