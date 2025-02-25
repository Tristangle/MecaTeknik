package mecateknik.db.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.mecateknik.db.AppDatabase
import com.example.mecateknik.db.dao.UserDao
import com.example.mecateknik.db.dao.CarDao
import com.example.mecateknik.db.entities.UserEntity
import com.example.mecateknik.db.entities.CarEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.UUID

@RunWith(AndroidJUnit4::class)
@SmallTest // Indique un test unitaire rapide
class UserDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var userDao: UserDao
    private lateinit var carDao: CarDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries() // ⚠️ Seulement pour les tests, à éviter en prod
            .build()
        userDao = database.userDao()
        carDao = database.carDao()
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        database.close() // 🔥 Ferme la base après chaque test
    }

    @Test
    fun testInsertUser() = runBlocking {
        val user = UserEntity(firebaseUid = "user-123", name = "Alice", email = "alice@example.com")
        val insertedId = userDao.insertUser(user)
        assert(insertedId > 0) // ✅ Vérifie que l'insertion a bien eu lieu
    }

    @Test
    fun testGetUserByFirebaseId() = runBlocking {
        val user = UserEntity(firebaseUid = "user-456", name = "Bob", email = "bob@example.com")
        userDao.insertUser(user)

        val retrievedUser = userDao.getUserByFirebaseId("user-456")
        assertNotNull(retrievedUser) // ✅ Vérifie que l’utilisateur existe
        assertEquals("Bob", retrievedUser?.name) // ✅ Vérifie que le nom correspond
    }

    @Test
    fun testUserWithMultipleCars() = runBlocking {
        val user = UserEntity(firebaseUid = "user-789", name = "Charlie", email = "charlie@example.com")
        userDao.insertUser(user)

        val car1 = CarEntity(
            id = UUID.randomUUID().toString(),
            userUid = "user-789",
            brand = "BMW",
            model = "M3",
            year = 2020,
            engineType = "Petrol",
            specification = "Sport",
            countryOrigin = "DE"
        )

        val car2 = CarEntity(
            id = UUID.randomUUID().toString(),
            userUid = "user-789",
            brand = "Audi",
            model = "RS5",
            year = 2021,
            engineType = "Petrol",
            specification = "Sport",
            countryOrigin = "DE"
        )

        carDao.insertCar(car1)
        carDao.insertCar(car2)

        val userWithCars = userDao.getUserWithCars("user-789")
        assertNotNull(userWithCars)
        assertEquals(2, userWithCars.cars.size) // ✅ Vérifie que l’utilisateur possède bien 2 voitures
        assertEquals("BMW", userWithCars.cars[0].brand) // ✅ Vérifie que la première voiture est bien une BMW
    }

    @Test
    fun testCascadeDeleteUserDeletesCars() = runBlocking {
        val user = UserEntity(firebaseUid = "user-999", name = "David", email = "david@example.com")
        userDao.insertUser(user)

        val car = CarEntity(
            id = UUID.randomUUID().toString(),
            userUid = "user-999",
            brand = "Tesla",
            model = "Model S",
            year = 2022,
            engineType = "Electric",
            specification = "Sedan",
            countryOrigin = "US"
        )

        carDao.insertCar(car)

        // 🔍 Vérification avant suppression
        val carsBeforeDelete = carDao.getCarsByUser("user-999")
        println("🚗 Voitures AVANT suppression de l'utilisateur : ${carsBeforeDelete.size}")
        assertEquals(1, carsBeforeDelete.size)

        // 🔥 Suppression de l'utilisateur
        userDao.getUserByFirebaseId("user-999")?.let {
            userDao.deleteUser(it)
        }

        // 🔄 Vérification après suppression
        val carsAfterDelete = carDao.getCarsByUser("user-999")
        println("🛑 Voitures APRÈS suppression de l'utilisateur : ${carsAfterDelete.size}")
        assertEquals(0, carsAfterDelete.size) // ✅ Vérifie que la voiture a bien été supprimée
    }


}
