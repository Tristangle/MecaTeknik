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

/**
 * Générateur automatique de pièces auto pour une voiture donnée.
 */
object AutoPartGenerator {

    private const val TAG = "AutoPartGenerator"

    // Liste de noms de pièces auto en français
    private val frenchPartNames = listOf(
        "Plaquettes de frein", "Kit d'embrayage", "Alternateur", "Batterie", "Bougie d'allumage",
        "Courroie de distribution", "Filtre à huile", "Filtre à air", "Pompe à carburant", "Radiateur",
        "Échappement", "Phare avant", "Feu arrière", "Amortisseur", "Pompe à eau",
        "Démarreur", "Turbocompresseur", "Support moteur", "Balai d'essuie-glace", "Thermostat",
        "Injecteur", "Joint de culasse", "Cardan", "Pompe de direction assistée", "Bobine d'allumage"
    )

    /**
     * Génère et insère 5 pièces auto pour la voiture donnée.
     * Chaque pièce est associée à l'ensemble {brand;model;year} de la voiture.
     */
    suspend fun generatePartsForCar(context: Context, car: CarEntity) {
        withContext(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(context)
            val autoPartDao: AutoPartDao = db.autoPartDao()

            val partsToInsert = mutableListOf<AutoPartEntity>()
            repeat(5) {
                // Génération d'une référence unique pour cette pièce
                val reference = generateUniqueReference(autoPartDao)
                // Sélection d'un nom de pièce aléatoire
                val name = frenchPartNames.random()
                // Génération d'un stock aléatoire entre 1 et 20
                val stock = Random.nextInt(1, 21)
                // Génération d'un prix aléatoire entre 20.0 et 500.0
                val price = Random.nextDouble(20.0, 500.0)
                // La pièce est associée à l'ensemble {brand;model;year} de la voiture
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
                    Log.d(TAG, "Pièce insérée: ${part.reference} - ${part.name}")
                }
            } else {
                Log.d(TAG, "Aucune nouvelle pièce générée pour la voiture ${car.brand} ${car.model} (${car.year})")
            }
        }
    }

    /**
     * Génère une référence unique sous la forme "AP-XXXX" en s'assurant qu'elle n'existe pas déjà.
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
