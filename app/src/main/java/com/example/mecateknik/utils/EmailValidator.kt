package com.example.mecateknik.utils

class EmailValidator {

    companion object {
        /**
         * Strict email regex based on the RFC 5322 standard.
         *
         * Regex breakdown:
         * `^(?=.{1,254}$)(?=.{1,64}@)`:
         *   - `^`: Matches the beginning of the string.
         *   - `(?=.{1,254}$)`: Positive lookahead assertion, ensuring the total email length is between 6 and 254 characters.
         *   - `(?=.{1,64}@)`: Positive lookahead assertion, ensuring the local part (before `@`) is between 1 and 64 characters.
         *
         * `[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+`: Matches the local part of the email (before `@`).
         *   - Allows alphanumeric characters and special characters: `.!#$%&'*+/=?^_`{|}~-`.
         *   - The `+` means one or more occurrences of these characters.
         *
         * `@`: Matches the literal `@` symbol.
         *
         * `[a-zA-Z0-9]`: Matches the beginning of the domain part.
         *
         * `(?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?`: Matches a domain label (part between dots).
         *   - Allows alphanumeric characters and hyphens.
         *   - Cannot start or end with a hyphen.
         *   - Length is between 1 and 62 characters (0 to 61 chars + at least one char at the end).
         *   - `?` makes this part optional
         *
         * `(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$`: Matches multiple domain labels separated by dots.
         *   - `\\.`: Matches a literal dot.
         *   - `*`: allows zero or more domain labels separated by dots.
         *   - `$`: Matches the end of the string.
         */
        private val EMAIL_REGEX = Regex(
            "^(?=.{6,254}$)(?=.{1,64}@)[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$"
        )

        // List of valid Top-Level Domains (TLDs).
        private val VALID_TLDS = setOf(
            "com", "org", "net", "int", "edu", "gov", "mil", "fr", "de", "uk", "ca", "us", "eu",
            "io", "tech", "dev", "xyz", "info", "biz", "co", "me", "ai", "tv"
        )
    }

    fun isValidEmail(email: String): Boolean {

        // Trim the email to remove spaces at the beginning and the end
        val trimmedEmail = email.trim()
        // Check if the email has been trimmed
        if(trimmedEmail != email) return false

        // Normalize the email by setting all the character in lower case
        val normalizedEmail = trimmedEmail.lowercase()

        // Check the email format with the regex
        if (!EMAIL_REGEX.matches(normalizedEmail)) {
            return false
        }

        // Extract the domain of the email
        val domain = normalizedEmail.substringAfter("@")

        // Extract the TLD from the domain.
        val tld = domain.substringAfterLast(".", missingDelimiterValue = "")

        // Check if the TLD id valid
        return tld in VALID_TLDS
    }
}

