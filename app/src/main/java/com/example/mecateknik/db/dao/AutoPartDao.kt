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

    @Query("""
        SELECT * FROM auto_parts 
        WHERE associated_models LIKE '%' || :configuration || '%'
    """)
    suspend fun getPartsByConfiguration(configuration: String): List<AutoPartEntity>

    @Query("""
        SELECT * FROM auto_parts 
        WHERE associated_models LIKE '%' || :configuration || '%' 
        AND name LIKE :partName
    """)
    suspend fun getPartsByConfigurationFiltered(configuration: String, partName: String): List<AutoPartEntity>

    @Query("""
        SELECT * FROM auto_parts 
        WHERE associated_models LIKE '%' || :configuration || '%'
    """)
    suspend fun getPartsByCarModel(configuration: String): List<AutoPartEntity>

    /**
     * Met à jour le stock d'une pièce auto.
     * @param reference Référence unique de la pièce.
     * @param newQuantity Nouvelle quantité en stock.
     */
    @Query("UPDATE auto_parts SET quantity_in_stock = :newQuantity WHERE reference = :reference")
    suspend fun updateStock(reference: String, newQuantity: Int)

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
