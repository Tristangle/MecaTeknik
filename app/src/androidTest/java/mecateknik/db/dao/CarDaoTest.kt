package mecateknik.db.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.mecateknik.db.AppDatabase
import com.example.mecateknik.db.dao.CarDao
import com.example.mecateknik.db.dao.UserDao
import com.example.mecateknik.db.entities.CarEntity
import com.example.mecateknik.db.entities.UserEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.UUID

@RunWith(AndroidJUnit4::class)
@SmallTest // Indique un test unitaire rapide
class CarDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var carDao: CarDao
    private lateinit var userDao: UserDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries() // ⚠️ Seulement pour les tests, à éviter en prod
            .build()
        carDao = database.carDao()
        userDao = database.userDao()

        // 🔹 Ajout d'un utilisateur pour respecter la relation de clé étrangère
        runBlocking {
            userDao.insertUser(UserEntity(firebaseUid = "test-user-uid", name = "John Doe", email = "john@example.com"))
        }
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        database.close() // 🔥 Ferme la base après chaque test
    }

    @Test
    fun testInsertCar() = runBlocking {
        // 🔹 Création d'une voiture liée à l'utilisateur
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

        val insertedId = carDao.insertCar(car)
        assert(insertedId > 0) // 🔥 Vérifie que l'insertion a bien eu lieu
    }

    @Test
    fun testGetCarsByUser() = runBlocking {
        val car = CarEntity(
            id = UUID.randomUUID().toString(),
            userUid = "test-user-uid",
            brand = "Nissan",
            model = "GT-R",
            year = 2021,
            engineType = "Petrol",
            specification = "Sport",
            countryOrigin = "JP"
        )
        carDao.insertCar(car)

        val cars = carDao.getCarsByUser("test-user-uid")
        assertEquals(1, cars.size)
        assertEquals("GT-R", cars[0].model)
    }

    @Test
    fun testDeleteCar() = runBlocking {
        val car = CarEntity(
            id = UUID.randomUUID().toString(),
            userUid = "test-user-uid",
            brand = "Mazda",
            model = "RX-7",
            year = 1995,
            engineType = "Rotary",
            specification = "Coupe",
            countryOrigin = "JP"
        )
        carDao.insertCar(car)

        val carsBeforeDelete = carDao.getCarsByUser("test-user-uid")
        assertEquals(1, carsBeforeDelete.size) // 🔹 Vérifie qu'on a bien une voiture avant suppression

        carDao.deleteCar(car)

        val carsAfterDelete = carDao.getCarsByUser("test-user-uid")
        assertEquals(0, carsAfterDelete.size) // 🔥 Vérifie que la voiture a été supprimée
    }

    @Test
    fun testGetCarsByCountry() = runBlocking {
        val carFR = CarEntity(
            id = UUID.randomUUID().toString(),
            userUid = "test-user-uid",
            brand = "Peugeot",
            model = "208",
            year = 2019,
            engineType = "Diesel",
            specification = "Hatchback",
            countryOrigin = "FR"
        )

        val carJP = CarEntity(
            id = UUID.randomUUID().toString(),
            userUid = "test-user-uid",
            brand = "Toyota",
            model = "Corolla",
            year = 2022,
            engineType = "Hybrid",
            specification = "Sedan",
            countryOrigin = "JP"
        )

        carDao.insertCar(carFR)
        carDao.insertCar(carJP)

        val frenchCars = carDao.getCarsByCountry("test-user-uid", "FR")
        assertEquals(1, frenchCars.size)
        assertEquals("Peugeot", frenchCars[0].brand)

        val japaneseCars = carDao.getCarsByCountry("test-user-uid", "JP")
        assertEquals(1, japaneseCars.size)
        assertEquals("Toyota", japaneseCars[0].brand)
    }
}
