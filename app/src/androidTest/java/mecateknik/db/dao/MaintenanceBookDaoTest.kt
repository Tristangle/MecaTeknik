package com.example.mecateknik.db.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.mecateknik.db.AppDatabase
import com.example.mecateknik.db.entities.CarEntity
import com.example.mecateknik.db.entities.MaintenanceBookEntity
import com.example.mecateknik.db.entities.UserEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.UUID

@RunWith(AndroidJUnit4::class)
@SmallTest
class MaintenanceBookDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var maintenanceBookDao: MaintenanceBookDao
    private lateinit var userDao: UserDao
    private lateinit var carDao: CarDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries() // ⚠️ Pour les tests uniquement
            .build()
        maintenanceBookDao = database.maintenanceBookDao()
        userDao = database.userDao()
        carDao = database.carDao()

        // 🔹 Ajout d'un utilisateur et d'une voiture pour tester
        runBlocking {
            userDao.insertUser(UserEntity(firebaseUid = "test-user-uid", name = "John Doe", email = "john@example.com"))

            carDao.insertCar(
                CarEntity(
                    id = "test-car-id",
                    userUid = "test-user-uid",
                    brand = "Toyota",
                    model = "Supra",
                    year = 2023,
                    engineType = "Hybrid",
                    specification = "Coupe",
                    countryOrigin = "JP"
                )
            )
        }
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        database.close()
    }

    @Test
    fun testInsertMaintenanceEntry() = runBlocking {
        val maintenance = MaintenanceBookEntity(
            id = UUID.randomUUID().toString(),
            carId = "test-car-id",
            date = System.currentTimeMillis(),
            description = "Vidange moteur",
            kilometers = 120000,
            garage = "Garage AutoSpeed"
        )

        val insertedId = maintenanceBookDao.insertMaintenance(maintenance)
        assert(insertedId > 0) // 🔥 Vérifie que l'insertion a bien eu lieu
    }

    @Test
    fun testGetMaintenanceForCar() = runBlocking {
        val maintenance1 = MaintenanceBookEntity(
            id = UUID.randomUUID().toString(),
            carId = "test-car-id",
            date = System.currentTimeMillis(),
            description = "Changement des plaquettes",
            kilometers = 130000,
            garage = "Garage Speedy"
        )

        val maintenance2 = MaintenanceBookEntity(
            id = UUID.randomUUID().toString(),
            carId = "test-car-id",
            date = System.currentTimeMillis(),
            description = "Remplacement batterie",
            kilometers = 135000,
            garage = "Norauto"
        )

        maintenanceBookDao.insertMaintenance(maintenance1)
        maintenanceBookDao.insertMaintenance(maintenance2)

        val maintenanceRecords = maintenanceBookDao.getMaintenanceForCar("test-car-id")
        assertEquals(2, maintenanceRecords.size) // ✅ Vérifie qu'on a bien 2 enregistrements

        assertTrue(maintenanceRecords.any { it.description == "Changement des plaquettes" })
        assertTrue(maintenanceRecords.any { it.description == "Remplacement batterie" })
    }

    @Test
    fun testDeleteMaintenanceEntry() = runBlocking {
        val maintenance = MaintenanceBookEntity(
            id = UUID.randomUUID().toString(),
            carId = "test-car-id",
            date = System.currentTimeMillis(),
            description = "Révision complète",
            kilometers = 140000,
            garage = "Feu Vert"
        )

        maintenanceBookDao.insertMaintenance(maintenance)

        val beforeDelete = maintenanceBookDao.getMaintenanceForCar("test-car-id")
        assertEquals(1, beforeDelete.size) // ✅ Vérifie qu'il y a bien 1 enregistrement avant suppression

        maintenanceBookDao.deleteMaintenance(maintenance)

        val afterDelete = maintenanceBookDao.getMaintenanceForCar("test-car-id")
        assertEquals(0, afterDelete.size) // 🔥 Vérifie que l'entrée a bien été supprimée
    }
    @Test
    fun testRetrieveMaintenanceSortedByDate() = runBlocking {
        val maintenance1 = MaintenanceBookEntity(
            id = UUID.randomUUID().toString(),
            carId = "test-car-id",
            date = 1700000000000, // Date ancienne
            description = "Vidange",
            kilometers = 60000,
            garage = "Garage A"
        )

        val maintenance2 = MaintenanceBookEntity(
            id = UUID.randomUUID().toString(),
            carId = "test-car-id",
            date = 1800000000000, // Date plus récente
            description = "Changement de freins",
            kilometers = 70000,
            garage = "Garage B"
        )

        maintenanceBookDao.insertMaintenance(maintenance1)
        maintenanceBookDao.insertMaintenance(maintenance2)

        val records = maintenanceBookDao.getMaintenanceForCar("test-car-id")
        assertEquals(2, records.size)
        assertEquals("Changement de freins", records[0].description) // ✅ Vérifie que l’ordre est correct (le plus récent en premier)
    }

    @Test
    fun testCascadeDeleteCarDeletesMaintenance() = runBlocking {
        val maintenance = MaintenanceBookEntity(
            id = UUID.randomUUID().toString(),
            carId = "test-car-id",
            date = System.currentTimeMillis(),
            description = "Vidange huile",
            kilometers = 125000,
            garage = "Garage Elite"
        )

        maintenanceBookDao.insertMaintenance(maintenance)

        val beforeDelete = maintenanceBookDao.getMaintenanceForCar("test-car-id")
        assertEquals(1, beforeDelete.size)

        // 🚗 Suppression de la voiture
        val car = carDao.getCarsByUser("test-user-uid").firstOrNull()
        car?.let { carDao.deleteCar(it) }

        // 🔥 Vérification que les entrées de maintenance sont bien supprimées en cascade
        val afterDelete = maintenanceBookDao.getMaintenanceForCar("test-car-id")
        assertEquals(0, afterDelete.size) // ✅ Il ne doit plus rien y avoir
    }
}
