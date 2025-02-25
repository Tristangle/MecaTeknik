package mecateknik.utils

import android.view.View
import androidx.test.espresso.*
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matcher

/**
 * Fonction utilitaire pour attendre qu'un élément soit visible dans l'UI avant de continuer.
 * @param viewMatcher Matcher de la vue à attendre
 * @param millis Temps d'attente maximum en millisecondes
 */
fun waitForView(viewMatcher: Matcher<View>, millis: Long): Boolean {
    val timeout = System.currentTimeMillis() + millis
    while (System.currentTimeMillis() < timeout) {
        try {
            onView(viewMatcher).check(matches(isDisplayed()))
            return true // ✅ Vue trouvée, sortie immédiate
        } catch (e: NoMatchingViewException) {
            Thread.sleep(50) // 🔥 Petite attente pour éviter une surcharge CPU
        }
    }
    return false // ❌ Vue non trouvée après timeout
}

/**
 * Fonction utilitaire pour forcer la déconnexion et suppression de l'utilisateur Firebase.
 */
fun forceSignOut() = runBlocking {
    val auth = FirebaseAuth.getInstance()
    auth.signOut() // 🔥 Déconnecte immédiatement

    // ✅ Vérifie si un utilisateur est encore connecté
    auth.currentUser?.let { user ->
        user.delete().addOnCompleteListener {
            println("✅ Utilisateur supprimé de Firebase")
        }
    }

    // 🕒 Attendre que Firebase détecte la suppression
    var retries = 0
    while (auth.currentUser != null && retries < 10) {
        delay(5000) // 🔥 Attente active (5 secondes par cycle)
        retries++
        println("⏳ Attente de la suppression... (Tentative $retries)")
    }

    println("✅ Déconnexion & suppression confirmées.")
}
