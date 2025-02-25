package com.example.mecateknik.db.entities

import org.junit.Assert.*
import org.junit.Test

class UserEntityTest {

    @Test
    fun testEntityCreation() {
        val user = UserEntity(
            firebaseUid = "test-firebase-uid",
            name = "John Doe",
            email = "john@example.com"
        )

        // ✅ Vérifie que les valeurs sont bien assignées
        assertEquals("test-firebase-uid", user.firebaseUid)
        assertEquals("John Doe", user.name)
        assertEquals("john@example.com", user.email)
    }

    @Test
    fun testUniqueFirebaseUid() {
        val user1 = UserEntity(
            firebaseUid = "firebase-uid-123",
            name = "Alice",
            email = "alice@example.com"
        )

        val user2 = UserEntity(
            firebaseUid = "firebase-uid-456",
            name = "Bob",
            email = "bob@example.com"
        )

        // ✅ Vérifie que les Firebase UID sont bien uniques
        assertNotEquals(user1.firebaseUid, user2.firebaseUid)
    }

    @Test
    fun testSerialization() {
        val user = UserEntity(
            firebaseUid = "test-firebase-uid",
            name = "Jane Doe",
            email = "jane@example.com"
        )

        val serialized = user.toString()
        assertTrue(serialized.contains("Jane Doe")) // ✅ Vérifie que le nom est bien présent
        assertTrue(serialized.contains("test-firebase-uid"))
        assertTrue(serialized.contains("jane@example.com"))
    }
}
