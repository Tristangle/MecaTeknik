package com.example.mecateknik.viewmodel.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mecateknik.db.dao.MaintenanceBookDao
import com.example.mecateknik.viewmodel.MaintenanceBookViewModel

class MaintenanceBookViewModelFactory(private val maintenanceBookDao: MaintenanceBookDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MaintenanceBookViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MaintenanceBookViewModel(maintenanceBookDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
