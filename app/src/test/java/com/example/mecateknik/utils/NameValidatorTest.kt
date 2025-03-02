package com.example.mecateknik.utils

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Classe de test pour la validation des noms.
 */
class NameValidatorTest {

    private val validator = NameValidator()

    /**
     * Teste que la méthode retourne true pour des noms valides.
     */
    @Test
    fun isValidName_returnsTrue_forValidNames() {
        val validNames = listOf(
            "Alice",
            "Jean Paul",
            "Marcel",
            "Émilie",
            "Olivier"
        )
        validNames.forEach { name ->
            assertTrue("Expected valid name for: $name", validator.isValidName(name))
        }
    }

    /**
     * Teste que la méthode retourne false pour des noms trop courts.
     */
    @Test
    fun isValidName_returnsFalse_forNamesTooShort() {
        val invalidNames = listOf(
            "Al",
            " A ",
            ""
        )
        invalidNames.forEach { name ->
            assertFalse("Expected invalid name for too short: $name", validator.isValidName(name))
        }
    }

    /**
     * Teste que la méthode retourne false pour des noms trop longs.
     */
    @Test
    fun isValidName_returnsFalse_forNamesTooLong() {
        val invalidName = "A".repeat(21)
        assertFalse("Expected invalid name for too long: $invalidName", validator.isValidName(invalidName))
    }

    /**
     * Teste que la méthode retourne false pour des noms contenant des caractères spéciaux.
     */
    @Test
    fun isValidName_returnsFalse_forNamesWithSpecialCharacters() {
        val invalidNames = listOf(
            "John_Doe",
            "Marie-Claire",
            "Olivier!",
            "Anna@",
            "Jean#"
        )
        invalidNames.forEach { name ->
            assertFalse("Expected invalid name for special characters in: $name", validator.isValidName(name))
        }
    }

    /**
     * Teste que la méthode gère correctement les espaces en début et en fin.
     */
    @Test
    fun isValidName_trimsWhitespaceBeforeValidation() {
        val validName = "  Alice  "
        assertTrue("Expected valid name after trimming whitespace for: $validName", validator.isValidName(validName))
    }
}
