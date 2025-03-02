package com.example.mecateknik.utils

/**
 * Classe utilitaire pour la validation des mots de passe.
 */
class PasswordValidator {

    companion object {
        private val PASSWORD_REGEX = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{10,}$")
    }

    /**
     * Vérifie si le mot de passe est valide selon les critères suivants :
     * - Au moins une majuscule
     * - Au moins une minuscule
     * - Au moins un chiffre
     * - Longueur minimale de 10 caractères
     *
     * @param password Le mot de passe à vérifier
     * @return true si le mot de passe est valide, false sinon
     */
    fun isValidPassword(password: String): Boolean {
        return PASSWORD_REGEX.matches(password)
    }
}
