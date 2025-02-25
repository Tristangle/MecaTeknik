package mecateknik.db.entities

import com.example.mecateknik.db.entities.MaintenanceBookEntity
import org.junit.Assert.*
import org.junit.Test
import java.util.*

class MaintenanceBookEntityTest {

    @Test
    fun testEntityCreation() {
        val maintenance = MaintenanceBookEntity(
            id = "test-maintenance-id",
            carId = "test-car-id",
            date = 1700000000000,
            description = "Vidange moteur",
            kilometers = 50000,
            garage = "Garage AutoSpeed"
        )

        // ✅ Vérifie que les valeurs sont correctement assignées
        assertEquals("test-maintenance-id", maintenance.id)
        assertEquals("test-car-id", maintenance.carId)
        assertEquals(1700000000000, maintenance.date)
        assertEquals("Vidange moteur", maintenance.description)
        assertEquals(50000, maintenance.kilometers)
        assertEquals("Garage AutoSpeed", maintenance.garage)
    }

    @Test
    fun testUniqueIdGeneration() {
        val maintenance1 = MaintenanceBookEntity(
            carId = "test-car-id",
            date = System.currentTimeMillis(),
            description = "Changement des plaquettes",
            kilometers = 60000,
            garage = "Garage AutoTech"
        )

        val maintenance2 = MaintenanceBookEntity(
            carId = "test-car-id",
            date = System.currentTimeMillis(),
            description = "Changement des pneus",
            kilometers = 75000,
            garage = "Garage FastTires"
        )

        // ✅ Vérifie que les ID générés sont uniques
        assertNotEquals(maintenance1.id, maintenance2.id)
    }

    @Test
    fun testSerialization() {
        val maintenance = MaintenanceBookEntity(
            id = UUID.randomUUID().toString(),
            carId = "test-car-id",
            date = 1700000000000,
            description = "Réglage moteur",
            kilometers = 100000,
            garage = null
        )

        val serialized = maintenance.toString()
        assertTrue(serialized.contains("Réglage moteur")) // ✅ Vérifie que la description est présente dans le toString()
        assertTrue(serialized.contains("test-car-id"))
    }
}
