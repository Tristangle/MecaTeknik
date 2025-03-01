package com.example.mecateknik.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.mecateknik.db.dao.CarDao
import com.example.mecateknik.db.dao.UserDao
import com.example.mecateknik.db.entities.CarEntity
import kotlinx.coroutines.launch

class VehicleViewModel(private val userDao: UserDao, private val carDao: CarDao) : ViewModel() {

    // 🔥 LiveData for vehicle info (Model + Year)
    private val _vehicleInfo = MutableLiveData("Select a vehicle")
    val vehicleInfo: LiveData<String> = _vehicleInfo

    // 🔥 LiveData for the user's cars
    private val _userCars = MutableLiveData<List<CarEntity>>()
    val userCars: LiveData<List<CarEntity>> = _userCars

    private fun updateVehicle(model: String, year: Int) {
        _vehicleInfo.postValue("$model ($year)") // 🔥 Safer way to update LiveData
    }

    // 🔹 Add car to user in database
    fun addCarToUser(firebaseUid: String, brand: String, model: String, year: Int, engineType: String, specification: String, countryOrigin: String) {
        viewModelScope.launch {
            val user = userDao.getUserByFirebaseId(firebaseUid)
            if (user != null) {
                val car = CarEntity(
                    userUid = user.firebaseUid,
                    brand = brand,
                    model = model,
                    year = year,
                    engineType = engineType,
                    specification = specification,
                    countryOrigin = countryOrigin
                )
                carDao.insertCar(car)
                updateVehicle(model, year) // 🔥 Updates UI
                getCarsByUser(firebaseUid) // 🔄 Refresh the user's car list
            } else {
                Log.e("VehicleViewModel", "User not found!")
            }
        }
    }

    // 🔹 Fetch user's cars from database and update LiveData
    fun getCarsByUser(firebaseUid: String) {
        viewModelScope.launch {
            val cars = carDao.getCarsByUser(firebaseUid)
            _userCars.postValue(cars) // 🔥 Updates LiveData with the car list
        }
    }

    fun deleteCar(car: CarEntity) {
        viewModelScope.launch {
            carDao.deleteCar(car)
            Log.d("VehicleViewModel", "❌ Voiture supprimée : ${car.brand} ${car.model}")
            getCarsByUser(car.userUid) // 🔄 Rafraîchir la liste après suppression
        }
    }

}
