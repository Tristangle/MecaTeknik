package mecateknik.db.entities

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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.UUID

@RunWith(AndroidJUnit4::class)
@SmallTest
class CarEntityTest {

    private lateinit var database: AppDatabase
    private lateinit var carDao: CarDao
    private lateinit var userDao: UserDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries() // ⚠️ Seulement pour les tests
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
    fun testInsertCarAndRetrieveById() = runBlocking {
        val carId = UUID.randomUUID().toString()
        val car = CarEntity(
            id = carId,
            userUid = "test-user-uid",
            brand = "Toyota",
            model = "Supra",
            year = 2023,
            engineType = "Hybrid",
            specification = "Coupe",
            countryOrigin = "JP"
        )

        carDao.insertCar(car)

        val cars = carDao.getCarsByUser("test-user-uid")
        assertEquals(1, cars.size)
        assertEquals(carId, cars[0].id) // ✅ Vérifie que l’ID correspond bien
        assertEquals("Supra", cars[0].model)
    }

    @Test
    fun testGetCarsByUser() = runBlocking {
        val car1 = CarEntity(
            id = UUID.randomUUID().toString(),
            userUid = "test-user-uid",
            brand = "Nissan",
            model = "GT-R",
            year = 2021,
            engineType = "Petrol",
            specification = "Sport",
            countryOrigin = "JP"
        )

        val car2 = CarEntity(
            id = UUID.randomUUID().toString(),
            userUid = "test-user-uid",
            brand = "BMW",
            model = "M3",
            year = 2022,
            engineType = "Petrol",
            specification = "Sedan",
            countryOrigin = "DE"
        )

        carDao.insertCar(car1)
        carDao.insertCar(car2)

        val cars = carDao.getCarsByUser("test-user-uid")
        assertEquals(2, cars.size) // ✅ Vérifie qu’on a bien 2 voitures

        assertTrue(cars.any { it.model == "GT-R" })
        assertTrue(cars.any { it.model == "M3" })
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

        val beforeDelete = carDao.getCarsByUser("test-user-uid")
        assertEquals(1, beforeDelete.size)

        carDao.deleteCar(car)

        val afterDelete = carDao.getCarsByUser("test-user-uid")
        assertEquals(0, afterDelete.size) // 🔥 Vérifie que la voiture a bien été supprimée
    }

    @Test
    fun testCascadeDeleteUserDeletesCars() = runBlocking {
        val car = CarEntity(
            id = UUID.randomUUID().toString(),
            userUid = "test-user-uid",
            brand = "Honda",
            model = "Civic",
            year = 2020,
            engineType = "Hybrid",
            specification = "Sedan",
            countryOrigin = "JP"
        )

        carDao.insertCar(car)

        val beforeDelete = carDao.getCarsByUser("test-user-uid")
        assertEquals(1, beforeDelete.size)

        // 🚗 Suppression de l’utilisateur
        val user = userDao.getUserByFirebaseId("test-user-uid")
        user?.let { userDao.deleteUser(it) }

        // 🔥 Vérification que la voiture a bien été supprimée en cascade
        val afterDelete = carDao.getCarsByUser("test-user-uid")
        assertEquals(0, afterDelete.size) // ✅ Il ne doit plus rien y avoir
    }
}
