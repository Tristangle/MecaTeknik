package com.example.mecateknik.ui.autoparts.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mecateknik.R
import com.example.mecateknik.db.entities.AutoPartEntity

/**
 * Adapter pour afficher les pièces auto sous forme de cards.
 */
class AutoPartAdapter : RecyclerView.Adapter<AutoPartAdapter.AutoPartViewHolder>() {

    private val autoParts = mutableListOf<AutoPartEntity>()

    fun submitList(parts: List<AutoPartEntity>) {
        autoParts.clear()
        autoParts.addAll(parts)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AutoPartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_auto_part, parent, false)
        return AutoPartViewHolder(view)
    }

    override fun onBindViewHolder(holder: AutoPartViewHolder, position: Int) {
        holder.bind(autoParts[position])
    }

    override fun getItemCount(): Int = autoParts.size

    class AutoPartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val partName: TextView = itemView.findViewById(R.id.textPartName)
        private val partReference: TextView = itemView.findViewById(R.id.textPartReference)
        private val partPrice: TextView = itemView.findViewById(R.id.textPartPrice)
        private val partStock: TextView = itemView.findViewById(R.id.textPartStock)

        fun bind(part: AutoPartEntity) {
            partName.text = part.name
            partReference.text = part.reference
            partPrice.text = "Prix: ${part.price} €"
            partStock.text = "Stock: ${part.quantityInStock}"
        }
    }
}
