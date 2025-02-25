package com.example.mecateknik.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.mecateknik.db.dao.MaintenanceBookDao
import com.example.mecateknik.db.entities.MaintenanceBookEntity
import kotlinx.coroutines.launch

class MaintenanceBookViewModel(private val maintenanceBookDao: MaintenanceBookDao) : ViewModel() {

    private val _maintenanceRecords = MutableLiveData<List<MaintenanceBookEntity>>()
    val maintenanceRecords: LiveData<List<MaintenanceBookEntity>> = _maintenanceRecords

    fun addMaintenanceRecord(carId: String, date: Long, description: String, kilometers: Int, garage: String?) {
        viewModelScope.launch {
            val maintenance = MaintenanceBookEntity(
                carId = carId,
                date = date,
                description = description,
                kilometers = kilometers,
                garage = garage
            )
            maintenanceBookDao.insertMaintenance(maintenance)
            loadMaintenanceRecords(carId)
        }
    }

    private fun loadMaintenanceRecords(carId: String) {
        viewModelScope.launch {
            _maintenanceRecords.postValue(maintenanceBookDao.getMaintenanceForCar(carId))
        }
    }
}
