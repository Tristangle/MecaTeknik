package com.example.mecateknik.ui.vehicle.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mecateknik.databinding.ItemVehicleCardBinding
import com.example.mecateknik.db.entities.CarEntity

class VehicleAdapter(private val onCarSelected: (CarEntity) -> Unit) :
    ListAdapter<CarEntity, VehicleAdapter.VehicleViewHolder>(VehicleDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        val binding = ItemVehicleCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VehicleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        val car = getItem(position)
        holder.bind(car)
        holder.itemView.setOnClickListener { onCarSelected(car) }
    }

    class VehicleViewHolder(private val binding: ItemVehicleCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(car: CarEntity) {
            binding.tvCarName.text = "${car.brand} ${car.model}"
            binding.tvCarYear.text = car.year.toString()
        }
    }
}

class VehicleDiffCallback : DiffUtil.ItemCallback<CarEntity>() {
    override fun areItemsTheSame(oldItem: CarEntity, newItem: CarEntity): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: CarEntity, newItem: CarEntity): Boolean = oldItem == newItem
}
