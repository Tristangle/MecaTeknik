package mecateknik.ui.login

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mecateknik.R
import com.example.mecateknik.ui.login.LoginActivity
import mecateknik.utils.ToastMatcher
import mecateknik.utils.forceSignOut
import mecateknik.utils.waitForView
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setUp() {
        Intents.init() // 🔥 Initialise Espresso Intents
        forceSignOut()
    }

    @After
    fun tearDown() {
        Intents.release() // ✅ Libère Espresso Intents
        activityRule.scenario.close()
        forceSignOut()
        Thread.sleep(10000)
    }

    @Test
    fun testALoginUIElementsDisplayed() {
        // ✅ Vérifie que les champs et le bouton sont bien visibles
        onView(withId(R.id.etEmail)).check(matches(isDisplayed()))
        onView(withId(R.id.etPassword)).check(matches(isDisplayed()))
        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()))
    }

    @Test
    fun testBLoginSuccess() {
        forceSignOut()
        val testEmail = "testuser@example.com"
        val testPassword = "password123"

        onView(withId(R.id.etEmail)).perform(typeText(testEmail), closeSoftKeyboard())
        onView(withId(R.id.etPassword)).perform(typeText(testPassword), closeSoftKeyboard())
        onView(withId(R.id.btnLogin)).perform(click())

        // 🔥 Attendre que la Toolbar soit bien visible
        val isToolbarDisplayed = waitForView(withId(R.id.toolbar), 5000)
        Assert.assertTrue("❌ La Toolbar ne s'est pas affichée !", isToolbarDisplayed)

        // ✅ Vérifie que la Toolbar est bien affichée
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()))
    }

    @Test
    fun testCLogout() {
        val testEmail = "testuser@example.com"
        val testPassword = "password123"

        // 🔹 Connexi>>Won de l'utilisateur
        onView(withId(R.id.etEmail)).perform(typeText(testEmail), closeSoftKeyboard())
        onView(withId(R.id.etPassword)).perform(typeText(testPassword), closeSoftKeyboard())
        onView(withId(R.id.btnLogin)).perform(click())

        // ✅ Attendre que `MainActivity` s'affiche
        assert(waitForView(withId(R.id.nav_view), 5000)) { "❌ `nav_view` non affiché après la connexion" }

        // ✅ Vérifie que le bouton `btnLogout` est bien affiché avant d'essayer de cliquer dessus
        assert(waitForView(withId(R.id.btnLogout), 5000)) { "❌ `btnLogout` non affiché" }

        println("✅ `btnLogout` trouvé, tentative de clic...")

        // 🔥 Clique sur le bouton de déconnexion
        onView(withId(R.id.btnLogout)).perform(click())

        println("✅ Clic sur `btnLogout` effectué, attente de redirection...")

        // ✅ Attendre que l'utilisateur soit redirigé vers `LoginActivity`
        assert(waitForView(withId(R.id.btnLogin), 5000)) { "❌ `btnLogin` non affiché après la déconnexion" }
    }

    @Test
    fun testDLoginFailure() {
        val testEmail = "wronguser@example.com"
        val testPassword = "wrongpassword"

        onView(withId(R.id.etEmail)).perform(typeText(testEmail), closeSoftKeyboard())
        onView(withId(R.id.etPassword)).perform(typeText(testPassword), closeSoftKeyboard())
        onView(withId(R.id.btnLogin)).perform(click())

        // 🔥 Attendre que le Toast apparaisse
        Thread.sleep(2000)

        onView(withText("Erreur de connexion"))
            .inRoot(ToastMatcher()) // 🔥 Utilise le ToastMatcher
            .check(matches(isDisplayed()))

    }

    @Test
    fun testEmptyFields() {
        // 🔹 Teste le comportement en laissant les champs vides et en appuyant sur le bouton
        onView(withId(R.id.btnLogin)).perform(click())

        // ✅ Vérifie que l'application affiche un message d'erreur "Veuillez remplir tous les champs"
        onView(withText("Veuillez remplir tous les champs")).check(matches(isDisplayed()))
    }
}
