package com.example.mecateknik.utils

/**
 * Classe utilitaire pour la validation des noms.
 */
class NameValidator {

    /**
     * Vérifie si le nom est valide.
     * Le nom doit contenir entre 3 et 20 caractères après suppression des espaces en début et fin
     * et ne doit contenir que des lettres et des espaces.
     *
     * @param name Le nom à vérifier.
     * @return true si le nom est valide, false sinon.
     */
    fun isValidName(name: String): Boolean {
        val trimmedName = name.trim()
        if (trimmedName.length < 3 || trimmedName.length > 20) return false
        return trimmedName.matches(Regex("^[\\p{L} ]+\$"))
    }
}
