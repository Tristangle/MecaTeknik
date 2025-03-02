package com.example.mecateknik.db.dao

import androidx.room.*
import com.example.mecateknik.db.entities.AutoPartEntity
import com.example.mecateknik.db.entities.AutoPartWithCar

/**
 * DAO pour la gestion des pièces auto dans la base de données.
 */
@Dao
interface AutoPartDao {

    /**
     * Insère une liste de pièces auto dans la base de données.
     * @param parts Liste des pièces auto.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(parts: List<AutoPartEntity>)

    /**
     * Recherche une pièce auto par sa référence.
     * @param reference Référence unique de la pièce.
     * @return La pièce auto correspondante, ou null si elle n'existe pas.
     */
    @Query("SELECT * FROM auto_parts WHERE reference = :reference")
    suspend fun getPartByReference(reference: String): AutoPartEntity?

    /**
     * Récupère une pièce auto avec son véhicule associé.
     * @param reference Référence unique de la pièce auto.
     * @return Une pièce auto avec les informations du véhicule.
     */
    @Transaction
    @Query("SELECT * FROM auto_parts WHERE reference = :reference")
    suspend fun getAutoPartWithCar(reference: String): AutoPartWithCar?

    /**
     * Récupère toutes les pièces auto compatibles avec un modèle de voiture.
     * @param model Nom du modèle du véhicule.
     * @return Liste des pièces compatibles avec ce modèle de voiture.
     */
    @Transaction
    @Query("""
        SELECT * FROM auto_parts 
        WHERE car_id IN (SELECT id FROM cars WHERE model = :model)
    """)
    suspend fun getPartsByCarModel(model: String): List<AutoPartEntity>

    /**
     * Supprime une pièce auto en base de données.
     * @param part Pièce auto à supprimer.
     */
    @Delete
    suspend fun deletePart(part: AutoPartEntity)

    /**
     * Supprime toutes les pièces auto de la base de données.
     */
    @Query("DELETE FROM auto_parts")
    suspend fun clearAllParts()
}
