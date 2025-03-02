package com.example.mecateknik.ui.autoparts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mecateknik.db.AppDatabase
import com.example.mecateknik.db.entities.AutoPartEntity
import com.example.mecateknik.db.entities.CarEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel gérant la recherche des pièces auto et la récupération des véhicules de l'utilisateur.
 */
class AutoPartSearchViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)

    private val _autoParts = MutableLiveData<List<AutoPartEntity>>()
    val autoParts: LiveData<List<AutoPartEntity>> get() = _autoParts

    private val _userCars = MutableLiveData<List<CarEntity>>()
    val userCars: LiveData<List<CarEntity>> get() = _userCars

    /**
     * Charge les voitures de l'utilisateur.
     */
    fun loadUserCars(userUid: String) {
        viewModelScope.launch {
            val cars = withContext(Dispatchers.IO) {
                db.carDao().getCarsByUser(userUid)
            }
            _userCars.postValue(cars)
        }
    }

    /**
     * Recherche les pièces auto compatibles avec la configuration donnée.
     * @param configuration La configuration sous la forme "brand;model;year".
     * @param partName Filtre optionnel sur le nom de la pièce.
     */
    fun searchAutoParts(configuration: String, partName: String?) {
        viewModelScope.launch {
            val parts = withContext(Dispatchers.IO) {
                if (partName.isNullOrEmpty()) {
                    db.autoPartDao().getPartsByConfiguration(configuration)
                } else {
                    db.autoPartDao().getPartsByConfigurationFiltered(configuration, "%$partName%")
                }
            }
            _autoParts.postValue(parts)
        }
    }
}
