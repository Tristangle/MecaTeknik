package mecateknik.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.mecateknik.db.AppDatabase
import com.example.mecateknik.db.dao.CarDao
import com.example.mecateknik.db.dao.MaintenanceBookDao
import com.example.mecateknik.db.dao.UserDao
import com.example.mecateknik.db.entities.CarEntity
import com.example.mecateknik.db.entities.MaintenanceBookEntity
import com.example.mecateknik.db.entities.UserEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*

@RunWith(AndroidJUnit4::class)
@SmallTest
class AppDatabaseTest {

    private lateinit var database: AppDatabase
    private lateinit var userDao: UserDao
    private lateinit var carDao: CarDao
    private lateinit var maintenanceBookDao: MaintenanceBookDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries() // ⚠️ Seulement pour les tests, à éviter en production
            .build()
        userDao = database.userDao()
        carDao = database.carDao()
        maintenanceBookDao = database.maintenanceBookDao()
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        database.close()
    }

    @Test
    fun testInsertAndRetrieveUser() = runBlocking {
        val user = UserEntity(
            firebaseUid = "test-user-uid",
            name = "John Doe",
            email = "john@example.com"
        )
        userDao.insertUser(user)

        val retrievedUser = userDao.getUserByFirebaseId("test-user-uid")
        assertNotNull(retrievedUser)
        assertEquals("John Doe", retrievedUser?.name)
    }

    @Test
    fun testInsertAndRetrieveCar() = runBlocking {
        val user = UserEntity(
            firebaseUid = "test-user-uid",
            name = "Alice",
            email = "alice@example.com"
        )
        userDao.insertUser(user)

        val car = CarEntity(
            id = UUID.randomUUID().toString(),
            userUid = "test-user-uid",
            brand = "Toyota",
            model = "Supra",
            year = 2023,
            engineType = "Hybrid",
            specification = "Coupe",
            countryOrigin = "JP"
        )
        carDao.insertCar(car)

        val retrievedCars = carDao.getCarsByUser("test-user-uid")
        assertEquals(1, retrievedCars.size)
        assertEquals("Toyota", retrievedCars[0].brand)
    }

    @Test
    fun testInsertAndRetrieveMaintenanceRecord() = runBlocking {
        val user = UserEntity(
            firebaseUid = "test-user-uid",
            name = "Bob",
            email = "bob@example.com"
        )
        userDao.insertUser(user)

        val car = CarEntity(
            id = "test-car-id",
            userUid = "test-user-uid",
            brand = "Nissan",
            model = "GT-R",
            year = 2021,
            engineType = "Petrol",
            specification = "Sport",
            countryOrigin = "JP"
        )
        carDao.insertCar(car)

        val maintenance = MaintenanceBookEntity(
            id = UUID.randomUUID().toString(),
            carId = "test-car-id",
            date = System.currentTimeMillis(),
            description = "Changement d’huile",
            kilometers = 60000,
            garage = "Garage AutoSpeed"
        )
        maintenanceBookDao.insertMaintenance(maintenance)

        val retrievedMaintenances = maintenanceBookDao.getMaintenanceForCar("test-car-id")
        assertEquals(1, retrievedMaintenances.size)
        assertEquals("Changement d’huile", retrievedMaintenances[0].description)
    }

    @Test
    fun testCascadeDeleteUser() = runBlocking {
        val user = UserEntity(
            firebaseUid = "test-user-uid",
            name = "Charlie",
            email = "charlie@example.com"
        )
        userDao.insertUser(user)

        val car = CarEntity(
            id = "test-car-id",
            userUid = "test-user-uid",
            brand = "Mazda",
            model = "RX-7",
            year = 1995,
            engineType = "Rotary",
            specification = "Coupe",
            countryOrigin = "JP"
        )
        carDao.insertCar(car)

        val maintenance = MaintenanceBookEntity(
            id = UUID.randomUUID().toString(),
            carId = "test-car-id",
            date = System.currentTimeMillis(),
            description = "Vidange moteur",
            kilometers = 80000,
            garage = "Garage SpeedTuning"
        )
        maintenanceBookDao.insertMaintenance(maintenance)

        // 🚀 Suppression de l’utilisateur
        userDao.deleteUser(user)

        // 🔥 Vérifie que l’utilisateur, sa voiture et ses entretiens sont bien supprimés
        val retrievedUser = userDao.getUserByFirebaseId("test-user-uid")
        assertNull(retrievedUser)

        val retrievedCars = carDao.getCarsByUser("test-user-uid")
        assertTrue(retrievedCars.isEmpty())

        val retrievedMaintenances = maintenanceBookDao.getMaintenanceForCar("test-car-id")
        assertTrue(retrievedMaintenances.isEmpty())
    }
}
