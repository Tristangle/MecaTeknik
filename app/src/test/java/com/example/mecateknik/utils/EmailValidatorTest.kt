package com.example.mecateknik.utils

import org.junit.Assert.*
import org.junit.Test

class EmailValidatorTest {

    private val emailValidator = EmailValidator()

    @Test
    fun isValidEmail_returnsTrue_forValidEmails() {
        val validEmails = listOf(
            "test@example.com",
            "user.name+tag@domain.co.uk",
            "email@sub.domain.com",
            "email@exemple.fr",
            "user_name@exemple.io",
            "user-name@exemple.dev",
            "contact@startup.tech",
            "hello@company.ai",
            "support@website.biz",
            "a@b.fr"
        )
        for (email in validEmails) {
            assertTrue("Expected true for $email", emailValidator.isValidEmail(email))
        }
    }

    @Test
    fun isValidEmail_returnsFalse_forEmailWithoutDomain() {
        assertFalse(emailValidator.isValidEmail("test@"))
    }

    @Test
    fun isValidEmail_returnsFalse_forEmailWithoutUsername() {
        assertFalse(emailValidator.isValidEmail("@example.com"))
    }

    @Test
    fun isValidEmail_returnsFalse_forEmailWithDoubleAtSymbol() {
        assertFalse(emailValidator.isValidEmail("user@@example.com"))
    }

    @Test
    fun isValidEmail_returnsFalse_forEmailWithLeadingDotInDomain() {
        assertFalse(emailValidator.isValidEmail("user@.com"))
    }

    @Test
    fun isValidEmail_returnsFalse_forEmailWithDoubleDotsInDomain() {
        assertFalse(emailValidator.isValidEmail("user@domain..com"))
    }

    @Test
    fun isValidEmail_returnsFalse_forEmailWithSpaceAtStart() {
        assertFalse(emailValidator.isValidEmail(" user@example.com"))
    }

    @Test
    fun isValidEmail_returnsFalse_forEmailWithSpaceAtEnd() {
        assertFalse(emailValidator.isValidEmail("user@example.com "))
    }

    @Test
    fun isValidEmail_returnsFalse_forEmailWithUnderscoreInDomain() {
        assertFalse(emailValidator.isValidEmail("user@ex_ample.com"))
    }

    @Test
    fun isValidEmail_returnsFalse_forEmailWithInvalidTLD() {
        assertFalse(emailValidator.isValidEmail("test@invalid.unknown"))
    }

    @Test
    fun isValidEmail_returnsFalse_forEmailWithLocalhostDomain() {
        assertFalse(emailValidator.isValidEmail("email@localhost"))
    }

    @Test
    fun isValidEmail_returnsFalse_forEmailWithNumericTLD() {
        assertFalse(emailValidator.isValidEmail("user@domain.c0m"))
    }

    @Test
    fun isValidEmail_returnsFalse_forEmailWithFakeTLD() {
        assertFalse(emailValidator.isValidEmail("user@domain.fake"))
    }

    @Test
    fun isValidEmail_returnsFalse_forEmailWithLeadingHyphenInDomain() {
        assertFalse(emailValidator.isValidEmail("user@-example.com"))
    }

    @Test
    fun isValidEmail_returnsFalse_forEmailWithTrailingHyphenInDomain() {
        assertFalse(emailValidator.isValidEmail("user@example-.com"))
    }

    @Test
    fun isValidEmail_returnsFalse_forEmailWithTrailingDotInDomain() {
        assertFalse(emailValidator.isValidEmail("user@example.com."))
    }

    @Test
    fun isValidEmail_returnsFalse_forEmailWithDoubleDotAnywhereInDomain() {
        assertFalse(emailValidator.isValidEmail("user@example..com"))
    }

    @Test
    fun isValidEmail_returnsFalse_forEmailWithSingleCharacterTLD() {
        assertFalse(emailValidator.isValidEmail("user@example.c"))
    }

    @Test
    fun isValidEmail_returnsFalse_forEmailWithInvalidCharacters() {
        assertFalse(emailValidator.isValidEmail("user@exa#mple.com"))
    }
}
