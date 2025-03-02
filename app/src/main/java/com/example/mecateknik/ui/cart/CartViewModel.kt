package com.example.mecateknik.ui.cart

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mecateknik.db.AppDatabase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val _cartItemsDetails = MutableLiveData<List<CartItemDetail>>()
    val cartItemsDetails: LiveData<List<CartItemDetail>> get() = _cartItemsDetails

    init {
        loadCartItems()
    }

    fun loadCartItems() {
        val userUid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        viewModelScope.launch {
            val cartItems = withContext(Dispatchers.IO) {
                db.cartDao().getCartItemsByUser(userUid)
            }
            val details = cartItems.map { cartItem ->
                val autoPart = withContext(Dispatchers.IO) {
                    db.autoPartDao().getPartByReference(cartItem.autoPartReference)
                }
                CartItemDetail(cartItem, autoPart)
            }
            _cartItemsDetails.postValue(details)
        }
    }

    fun deleteCartItem(detail: CartItemDetail) {
        viewModelScope.launch(Dispatchers.IO) {
            db.cartDao().deleteCartItem(detail.cartItem)
            loadCartItems()
        }
    }

    fun getTotalPrice(details: List<CartItemDetail>): Double {
        return details.sumOf { detail ->
            val price = detail.autoPart?.price ?: 0.0
            price * detail.cartItem.quantity
        }
    }

    /**
     * Checkout : Met à jour le stock de chaque pièce achetée et vide le panier
     */
    fun checkoutCart() {
        val userUid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        viewModelScope.launch(Dispatchers.IO) {
            val cartItems = db.cartDao().getCartItemsByUser(userUid)
            cartItems.forEach { cartItem ->
                val part = db.autoPartDao().getPartByReference(cartItem.autoPartReference)
                if (part != null) {
                    val newStock = (part.quantityInStock - cartItem.quantity).coerceAtLeast(0)
                    db.autoPartDao().updateStock(part.reference, newStock)
                }
            }
            db.cartDao().clearCartForUser(userUid)
            loadCartItems()
        }
    }
}
