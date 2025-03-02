package com.example.mecateknik.utils

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Classe de test pour la validation des mots de passe.
 */
class PasswordValidatorTest {

    private val validator = PasswordValidator()

    /**
     * Teste que la méthode retourne true pour des mots de passe valides.
     */
    @Test
    fun isValidPassword_returnsTrue_forValidPasswords() {
        val validPasswords = listOf(
            "Password123",
            "Abcdefghij1",
            "StrongPass1",
            "A1b2C3d4E5"
        )
        validPasswords.forEach { password ->
            assertTrue("Expected valid password for: $password", validator.isValidPassword(password))
        }
    }

    /**
     * Teste que la méthode retourne false pour un mot de passe sans majuscule.
     */
    @Test
    fun isValidPassword_returnsFalse_forPasswordWithoutUppercase() {
        val invalidPassword = "password1234"
        assertFalse("Expected invalid password for: $invalidPassword", validator.isValidPassword(invalidPassword))
    }

    /**
     * Teste que la méthode retourne false pour un mot de passe sans minuscule.
     */
    @Test
    fun isValidPassword_returnsFalse_forPasswordWithoutLowercase() {
        val invalidPassword = "PASSWORD1234"
        assertFalse("Expected invalid password for: $invalidPassword", validator.isValidPassword(invalidPassword))
    }

    /**
     * Teste que la méthode retourne false pour un mot de passe sans chiffre.
     */
    @Test
    fun isValidPassword_returnsFalse_forPasswordWithoutDigit() {
        val invalidPassword = "PasswordNoDigits"
        assertFalse("Expected invalid password for: $invalidPassword", validator.isValidPassword(invalidPassword))
    }

    /**
     * Teste que la méthode retourne false pour un mot de passe trop court.
     */
    @Test
    fun isValidPassword_returnsFalse_forPasswordTooShort() {
        val invalidPassword = "Pass1"
        assertFalse("Expected invalid password for: $invalidPassword", validator.isValidPassword(invalidPassword))
    }
}
