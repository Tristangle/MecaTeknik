package com.example.mecateknik.ui.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mecateknik.R

class CartAdapter(private val onRemoveClicked: (CartItemDetail) -> Unit) :
    ListAdapter<CartItemDetail, CartAdapter.CartViewHolder>(CartDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val detail = getItem(position)
        holder.bind(detail)
        holder.itemView.findViewById<TextView>(R.id.btnRemove)?.setOnClickListener {
            onRemoveClicked(detail)
        }
    }

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.tvPartName)
        private val tvReference: TextView = itemView.findViewById(R.id.tvPartReference)
        private val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        private val tvQuantity: TextView = itemView.findViewById(R.id.tvQuantity)

        fun bind(detail: CartItemDetail) {
            val part = detail.autoPart
            if (part != null) {
                tvName.text = part.name
                tvReference.text = "Réf: ${part.reference}"
                tvPrice.text = "Prix: ${String.format("%.2f", part.price)} €"
            } else {
                tvName.text = "Inconnu"
                tvReference.text = ""
                tvPrice.text = ""
            }
            tvQuantity.text = "Quantité: ${detail.cartItem.quantity}"
        }
    }
}

class CartDiffCallback : DiffUtil.ItemCallback<CartItemDetail>() {
    override fun areItemsTheSame(oldItem: CartItemDetail, newItem: CartItemDetail): Boolean {
        return oldItem.cartItem.id == newItem.cartItem.id
    }

    override fun areContentsTheSame(oldItem: CartItemDetail, newItem: CartItemDetail): Boolean {
        return oldItem == newItem
    }
}
