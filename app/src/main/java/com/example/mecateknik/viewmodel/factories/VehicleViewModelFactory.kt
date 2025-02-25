package com.example.mecateknik.viewmodel.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mecateknik.db.dao.CarDao
import com.example.mecateknik.db.dao.UserDao
import com.example.mecateknik.viewmodel.VehicleViewModel

class VehicleViewModelFactory(private val userDao: UserDao, private val carDao: CarDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VehicleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VehicleViewModel(userDao, carDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
