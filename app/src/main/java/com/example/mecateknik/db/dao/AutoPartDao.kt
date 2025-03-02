package com.example.mecateknik.db.dao

import androidx.room.*
import com.example.mecateknik.db.entities.AutoPartEntity

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
