package com.example.mecateknik.utils

import android.content.Context
import android.util.Log
import com.example.mecateknik.db.AppDatabase
import com.example.mecateknik.db.dao.AutoPartDao
import com.example.mecateknik.db.entities.AutoPartEntity
import com.example.mecateknik.db.entities.CarEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random

object AutoPartGenerator {

    private const val TAG = "AutoPartGenerator"

    // Liste étendue (~30 noms) de pièces auto en français
    private val frenchPartNames = listOf(
        "Plaquettes de frein",
        "Kit d'embrayage",
        "Alternateur",
        "Batterie",
        "Bougie d'allumage",
        "Courroie de distribution",
        "Filtre à huile",
        "Filtre à air",
        "Pompe à carburant",
        "Radiateur",
        "Échappement",
        "Phare avant",
        "Feu arrière",
        "Amortisseur",
        "Pompe à eau",
        "Démarreur",
        "Turbocompresseur",
        "Support moteur",
        "Balai d'essuie-glace",
        "Thermostat",
        "Injecteur",
        "Joint de culasse",
        "Cardan",
        "Pompe de direction assistée",
        "Bobine d'allumage",
        "Compresseur",
        "Moteur de ventilation",
        "Ventilateur de refroidissement",
        "Capteur d'oxygène",
        "Bouchon de radiateur"
    )

    /**
     * Génère et insère 8 pièces auto pour la voiture donnée.
     * Chaque pièce est associée à l'ensemble {brand;model;year} de la voiture.
     * Les noms générés sont uniques pour la voiture.
     */
    suspend fun generatePartsForCar(context: Context, car: CarEntity) {
        withContext(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(context)
            val autoPartDao: AutoPartDao = db.autoPartDao()

            // Récupérer les pièces déjà générées pour cette voiture (pour éviter les doublons)
            val configuration = "${car.brand};${car.model};${car.year}"
            val existingParts = autoPartDao.getPartsByCarModel(configuration)
            val usedNames = existingParts.map { it.name }.toMutableSet()

            val partsToInsert = mutableListOf<AutoPartEntity>()
            // On génère jusqu'à 8 pièces ou jusqu'à épuisement des noms disponibles
            var attempts = 0
            while (partsToInsert.size < 8 && attempts < 20) {
                attempts++
                val availableNames = frenchPartNames.filter { it !in usedNames }
                if (availableNames.isEmpty()) break  // Plus de noms disponibles
                val name = availableNames.random()
                usedNames.add(name)
                val reference = generateUniqueReference(autoPartDao)
                val stock = Random.nextInt(1, 21)
                // Génère un prix entre 20.0 et 500.0 et arrondi à 2 décimales
                val rawPrice = Random.nextDouble(20.0, 500.0)
                val price = (rawPrice * 100).toInt() / 100.0
                // La configuration principale au format "brand;model;year"
                val associatedModels = listOf("${car.brand};${car.model};${car.year}")

                val autoPart = AutoPartEntity(
                    reference = reference,
                    name = name,
                    quantityInStock = stock,
                    price = price,
                    associatedModels = associatedModels
                )
                Log.d(TAG, "Génération pièce: ref=$reference, nom=$name, stock=$stock, prix=$price, associé=${associatedModels.first()}")
                partsToInsert.add(autoPart)
            }
            if (partsToInsert.isNotEmpty()) {
                autoPartDao.insertAll(partsToInsert)
                Log.d(TAG, "Insertion de ${partsToInsert.size} pièces pour la voiture ${car.brand} ${car.model} (${car.year})")
                partsToInsert.forEach { part ->
                    Log.d(TAG, "Pièce insérée: ${part.reference} - ${part.name} - Prix: ${part.price} €")
                }
            } else {
                Log.d(TAG, "Aucune nouvelle pièce générée pour la voiture ${car.brand} ${car.model} (${car.year})")
            }
        }
    }

    /**
     * Génère une référence unique sous la forme "AP-XXXX", en s'assurant qu'elle n'existe pas déjà.
     */
    private suspend fun generateUniqueReference(autoPartDao: AutoPartDao): String {
        var reference: String
        do {
            reference = "AP-${Random.nextInt(1000, 9999)}"
            Log.d(TAG, "Tentative de référence: $reference")
        } while (autoPartDao.getPartByReference(reference) != null)
        Log.d(TAG, "Référence unique générée: $reference")
        return reference
    }
}
