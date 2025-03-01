package com.example.mecateknik.ui.maintenance

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mecateknik.databinding.ItemMaintenanceBinding
import com.example.mecateknik.db.entities.MaintenanceBookEntity

class MaintenanceAdapter : ListAdapter<MaintenanceBookEntity, MaintenanceAdapter.MaintenanceViewHolder>(MaintenanceDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaintenanceViewHolder {
        val binding = ItemMaintenanceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MaintenanceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MaintenanceViewHolder, position: Int) {
        val record = getItem(position)
        holder.bind(record)
    }

    class MaintenanceViewHolder(private val binding: ItemMaintenanceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(record: MaintenanceBookEntity) {
            binding.tvDescription.text = record.description
            binding.tvDate.text = record.date.toString() // Idéalement, formate la date
            binding.tvKilometers.text = record.kilometers.toString()
            binding.tvGarage.text = record.garage ?: ""
        }
    }
}

class MaintenanceDiffCallback : DiffUtil.ItemCallback<MaintenanceBookEntity>() {
    override fun areItemsTheSame(oldItem: MaintenanceBookEntity, newItem: MaintenanceBookEntity): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: MaintenanceBookEntity, newItem: MaintenanceBookEntity): Boolean =
        oldItem == newItem
}
